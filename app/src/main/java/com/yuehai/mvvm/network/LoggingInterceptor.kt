package com.yuehai.mvvm.network

import android.util.Log
import com.yuehai.mvvm.BuildConfig
import com.yuehai.mvvm.util.StringUtil
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.StatusLine
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class LoggingInterceptor : Interceptor {
    private fun log(message: String) {
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
            || (message.startsWith("[") && message.endsWith("]"))
        ) {
            Log.d("HTTP", StringUtil.formatJson(StringUtil.decodeUnicode(message)))
        } else {
            Log.d("HTTP", message)
        }
    }

    @Volatile
    private var headersToRedact = emptySet<String>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!BuildConfig.DEBUG) {
            return chain.proceed(request)
        }
        val requestBody = request.body()
        val connection = chain.connection()
        var requestStartMessage =
            ("--> ${request.method()} ${request.url()}${if (connection != null) " " + connection.protocol() else ""}")
        if (requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        val requestLog = StringBuilder(" \n").append(requestStartMessage)
        val headers = request.headers()
        if (requestBody != null) {
            requestBody.contentType()?.let {
                if (headers["Content-Type"] == null) {
                    requestLog.append("\n").append("Content-Type: $it")
                }
            }
            if (requestBody.contentLength() != -1L) {
                if (headers["Content-Length"] == null) {
                    requestLog.append("\n").append("Content-Length: ${requestBody.contentLength()}")
                }
            }
        }
        for (i in 0 until headers.size()) {
            requestLog.append("\n").append(
                (headers.name(i) + ": " + if (headers.name(i) in headersToRedact) "██" else headers.value(
                    i
                ))
            )
        }
        if (requestBody == null) {
            requestLog.append("\n").append("--> END ${request.method()}")
        } else if (bodyHasUnknownEncoding(request.headers())) {
            requestLog.append("\n").append("--> END ${request.method()} (encoded body omitted)")
        } else if (requestBody.isDuplex) {
            requestLog.append("\n")
                .append("--> END ${request.method()} (duplex request body omitted)")
        } else if (requestBody.isOneShot) {
            requestLog.append("\n").append("--> END ${request.method()} (one-shot body omitted)")
        } else {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val contentType = requestBody.contentType()
            val charset: Charset =
                contentType?.charset(Charset.forName("UTF-8")) ?: Charset.forName("UTF-8")
            requestLog.append("\n").append("")
            if (buffer.isProbablyUtf8()) {
                requestLog.append("\n").append(buffer.readString(charset))
                requestLog.append("\n")
                    .append("--> END ${request.method()} (${requestBody.contentLength()}-byte body)")
            } else {
                requestLog.append("\n")
                    .append("--> END ${request.method()} (binary ${requestBody.contentLength()}-byte body omitted)")
            }
        }
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            requestLog.append("\n").append("<-- HTTP FAILED: $e")
            throw e
        }
        log(requestLog.toString())
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()
        if (responseBody != null) {
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            log(
                " \n<-- ${response.code()}${
                    if (response.message().isEmpty()) "" else ' ' + response.message()
                } ${response.request().url()} (${tookMs}ms${", $bodySize body"})"
            )
            if (!response.promisesBody()) {
                log("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers())) {
                log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer
                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size()
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }
                val contentType = responseBody.contentType()
                val charset: Charset =
                    contentType?.charset(Charset.forName("UTF-8")) ?: Charset.forName("UTF-8")
                if (!buffer.isProbablyUtf8()) {
                    log("")
                    log("<-- END HTTP (binary ${buffer.size()}-byte body omitted)")
                    return response
                }
                if (contentLength != 0L) {
                    log("")
                    log(buffer.clone().readString(charset))
                }
                if (gzippedLength != null) {
                    log("<-- END HTTP (${buffer.size()}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    log("<-- END HTTP (${buffer.size()}-byte body)")
                }
            }
        }
        return response
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun Response.promisesBody(): Boolean {
        if (request().method() == "HEAD") {
            return false
        }
        val responseCode = code()
        if ((responseCode < StatusLine.HTTP_CONTINUE || responseCode >= 200) &&
            responseCode != HttpURLConnection.HTTP_NO_CONTENT &&
            responseCode != HttpURLConnection.HTTP_NOT_MODIFIED
        ) {
            return true
        }
        if (headersContentLength() != -1L ||
            "chunked".equals(header("Transfer-Encoding"), ignoreCase = true)
        ) {
            return true
        }

        return false
    }

    private fun Response.headersContentLength(): Long {
        return headers()["Content-Length"]?.toLongOrDefault(-1L) ?: -1L
    }

    private fun String.toLongOrDefault(defaultValue: Long): Long {
        return try {
            toLong()
        } catch (_: NumberFormatException) {
            defaultValue
        }
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size().coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }
}
