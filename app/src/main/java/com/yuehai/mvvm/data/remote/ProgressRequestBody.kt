package com.yuehai.mvvm.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.*
import java.io.IOException

class ProgressRequestBody internal constructor(
    private val multipartBody: MultipartBody,
    private val callback: (Long) -> Unit
) : RequestBody() {
    //okHttpLog拦截器导致重读写入问题，用于第二次实际写入的数据判断 显示progressListener
    private var isRealWrite = false

    override fun contentType() = multipartBody.contentType()

    override fun contentLength() = multipartBody.contentLength()

    override fun writeTo(sink: BufferedSink) {
        val bufferedSink = Okio.buffer(CountingSink(sink))
        //写入
        multipartBody.writeTo(bufferedSink)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush()
        isRealWrite = true
    }

    private inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
        private var bytesWritten = 0L
        private var totalLength = -1L
        private var currentMB = 0L

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (isRealWrite) {
                if (totalLength == -1L) {
                    totalLength = contentLength()
                }
                bytesWritten += byteCount
                (bytesWritten / 1024 / 1024).let {
                    if (currentMB != it) {
                        currentMB = it
                        callback.invoke(bytesWritten * 100 / totalLength)
                    }
                }
            }
        }
    }
}