package com.yuehai.mvvm.data.remote

import com.yuehai.mvvm.util.StringUtil
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.StatusLine
import okhttp3.internal.platform.Platform
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class LoggingInterceptor @JvmOverloads constructor(
    private val logger: Logger = Logger.DEFAULT
) : Interceptor {

    @set:JvmName("level")
    @Volatile
    var level = Level.BASIC

    enum class Level {
        /**
         * No logs.
         */
        NONE,

        /**
         * Logs request and response lines.
         */
        BASIC,

        /**
         * Logs request and response lines and their respective headers.
         */
        HEADERS,

        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         */
        BODY
    }

    fun interface Logger {
        fun log(message: String)

        companion object {
            /** A [Logger] defaults output appropriate for the current platform. */
            @JvmField
            val DEFAULT: Logger = DefaultLogger()

            private class DefaultLogger : Logger {
                override fun log(message: String) {
                    Platform.get().log(Platform.INFO, message, null)
                }
            }
        }
    }

    fun setLevel(level: Level) = apply {
        this.level = level
    }

    private fun formatMessage(message: String) =
        if ((message.startsWith("{") && message.endsWith("}"))
            || (message.startsWith("[") && message.endsWith("]"))
        ) {
            StringUtil.formatJson(StringUtil.decodeUnicode(message))
        } else {
            message
        }

    @Volatile
    private var headersToRedact = emptySet<String>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }
        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body()
        val connection = chain.connection()
        var requestStartMessage =
            ("--> ${request.method()} ${request.url()}${if (connection != null) " " + connection.protocol() else ""}")
        if (requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        val requestLog = StringBuilder(" \n").append(requestStartMessage)
        if (logHeaders) {
            val headers = request.headers()
            if (requestBody != null) {
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        requestLog.append("\n").append("Content-Type: $it")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        requestLog.append("\n")
                            .append("Content-Length: ${requestBody.contentLength()}")
                    }
                }
            }
            appendHeader(requestLog, headers)
        }
        if (!logBody || requestBody == null) {
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
            if (buffer.isProbablyUtf8()) {
                requestLog.append(formatMessage(buffer.readString(charset)))
                requestLog.append("\n")
                    .append("--> END ${request.method()} (${requestBody.contentLength()}-byte body)")
            } else {
                requestLog.append("\n")
                    .append("--> END ${request.method()} (binary ${requestBody.contentLength()}-byte body omitted)")
            }
        }
        logger.log(requestLog.toString())
        //response
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }
        val responseLog = StringBuilder()
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()
        if (logHeaders) {
            appendHeader(responseLog, response.headers())
        }
        if (responseBody != null) {
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            responseLog.append(
                " \n<-- ${response.code()}${
                    if (response.message().isEmpty()) "" else ' ' + response.message()
                } ${response.request().url()} (${tookMs}ms${", $bodySize body"})"
            )
            if (!logBody || !response.promisesBody() || responseBody.contentType()
                    .toString() != "application/json"
            ) {
                responseLog.append(" \n<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers())) {
                responseLog.append(" \n<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer
                var gzippedLength: Long? = null
                if ("gzip".equals(response.headers()["Content-Encoding"], ignoreCase = true)) {
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
                    responseLog.append(" \n<-- END HTTP (binary ${buffer.size()}-byte body omitted)")
                    logger.log(responseLog.toString())
                    return response
                }
                if (contentLength != 0L) {
                    responseLog.append(formatMessage(buffer.clone().readString(charset)))
                }
                if (gzippedLength != null) {
                    responseLog.append(
                        " \n<-- END HTTP (${buffer.size()}-byte, $gzippedLength-gzipped-byte body)"
                    )
                } else {
                    responseLog.append(" \n<-- END HTTP (${buffer.size()}-byte body)")
                }
            }
        }
        logger.log(responseLog.toString())
        return response
    }

    private fun appendHeader(sb: StringBuilder, headers: Headers) {
        for (i in 0 until headers.size()) {
            sb.append(
                " \n${headers.name(i)}: ${
                    if (headers.name(i) in headersToRedact) "██" else headers.value(
                        i
                    )
                }"
            )
        }
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
