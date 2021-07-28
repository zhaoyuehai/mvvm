package com.yuehai.mvvm.data

import android.app.Application
import com.google.gson.stream.MalformedJsonException
import com.yuehai.mvvm.data.model.ErrorModel
import com.yuehai.mvvm.data.model.ResultModel
import com.yuehai.mvvm.data.model.TestData
import com.yuehai.mvvm.data.remote.ApiService
import com.yuehai.mvvm.data.remote.ProgressRequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLException

/**
 * Created by zhaoyuehai 2021/4/27
 */
@Singleton
open class DataRepository @Inject constructor(
    private val apiService: ApiService,
    val application: Application
) {

    fun loadData(scope: CoroutineScope, callback: (ResultModel<TestData>) -> Unit) {
        request(scope, callback) {
            apiService.testData()
        }
    }

    fun loadListData(scope: CoroutineScope, callback: (ResultModel<List<TestData>>) -> Unit) {
        request(scope, callback) {
            apiService.testListData()
        }
    }

    private fun <T : Any> request(
        scope: CoroutineScope,
        callback: (ResultModel<T>) -> Unit,
        block: suspend CoroutineScope.() -> ResultModel<T>
    ) = scope.launch(Dispatchers.IO) {
        try {
            val result = block()
            withContext(Dispatchers.Main) {
//                if (result.code == 401) {
//                    myApp.reLogin()
//                }
                callback.invoke(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = when (e) {
                is HttpException -> "服务器连接异常"//404 500 服务器连接错误
                is IOException -> {
                    when (e) {
                        is UnknownHostException -> "请求失败，请重试！"
                        is SocketTimeoutException -> "请求超时"
                        //连上网，连接超时无响应
                        is SocketException -> {
                            when {
                                e is ConnectException -> "网络连接异常" //断网了
                                e.message == "Socket closed" -> "请求已取消"
                                else -> e.message
                            }
                        }
                        is MalformedJsonException -> "数据解析失败"//gson解析异常
                        else -> if (e.message == "Canceled") "请求已取消" else e.message
                    }
                }
                else -> e.message
            }
            withContext(Dispatchers.Main) {
                callback.invoke(ErrorModel(msg))
            }
        }
    }

    private fun getMessage(e: Exception) = when (e) {
        is HttpException -> "服务器连接异常"//404 500 服务器连接错误
        is IOException -> {
            when (e) {
                is UnknownHostException -> "请求失败，请重试！"
                is SocketTimeoutException -> "请求超时"
                is SSLException -> "网络连接异常"
                //连上网，连接超时无响应
                is SocketException -> {
                    when {
                        e is ConnectException -> "网络连接异常" //断网了
                        e.message == "Socket closed" -> "请求已取消"
                        else -> e.message
                    }
                }
                is MalformedJsonException -> "数据解析失败"//gson解析异常
                else -> if (e.message == "Canceled") "请求已取消" else e.message
            }
        }
        else -> e.message
    }

    /**
     * 文件下载
     * 注意：防止被Interceptor拦截
     * responseBody.contentType().toString() == "application/json"
     *
    fun download() {
    showLoading("正在下载...")
    repository.download(
    viewModelScope,
    "https://power.cnecloud.com/upgrade/app-power-release.apk",
    "app-power-release.apk", object : ProgressListener {
    override fun onProgress(percent: Long, total: Long) {
    progressText.value = "下载进度：${percent}% / ${total}B"
    showLoading("${percent}%...")
    }

    override fun onResult(total: Long?, message: String) {
    showToast(message)
    dismissLoading()
    if (total != null) {
    progressText.value = "下载完成 ${total}B"
    }
    }
    })
    }
     */
    fun download(
        scope: CoroutineScope,
        url: String,
        fileName: String,
        listener: ProgressListener
    ) = scope.launch(Dispatchers.IO) {
        try {
            val response = apiService.download(url)
            val body = response.body() ?: throw RuntimeException("下载失败")
            val totalLength = body.contentLength()
            if (totalLength <= 0) throw RuntimeException("下载失败")
            val ios = body.byteStream()
            val ops =
                FileOutputStream(File("${application.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS)}${java.io.File.separator}$fileName"))
            var currentLength = 0L
            val bufferSize = 1024 * 8
            val buffer = ByteArray(bufferSize)
            val bufferedInputStream = BufferedInputStream(ios, bufferSize)
            var readLength: Int
            var currentMB = 0L
            while (bufferedInputStream.read(buffer, 0, bufferSize)
                    .also { readLength = it } != -1
            ) {
                ops.write(buffer, 0, readLength)
                currentLength += readLength
                (currentLength / 1024 / 1024).let { //大于1M显示进度
                    if (it != currentMB) {
                        currentMB = it
                        withContext(Dispatchers.Main) {
                            listener.onProgress(currentLength * 100 / totalLength, totalLength)
                        }
                    }
                }
            }
            bufferedInputStream.close()
            ops.close()
            ios.close()
            withContext(Dispatchers.Main) {
                listener.onResult(totalLength, "下载成功")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                listener.onResult(null, getMessage(e) ?: "下载失败")
            }
        }
    }

    /**
     * 文件上传
     *
    fun upload(uri: Uri) {
    val filePath = FileUtil.getPath(repository.myApp, uri)
    if (filePath.isNullOrEmpty()) {
    showToast("文件未找到")
    return
    }
    showLoading("正在上传...")
    repository.upload(viewModelScope, listOf(File(filePath)), object : ProgressListener {
    override fun onProgress(percent: Long, total: Long) {
    progressText.value = "上传进度：${percent}% / ${total}B"
    showLoading("${percent}%...")
    }

    override fun onResult(total: Long?, message: String) {
    showToast(message)
    dismissLoading()
    if (total != null) {
    progressText.value = "上传完成 ${total}B"
    }
    }
    })
    }
     */
    fun upload(
        scope: CoroutineScope,
        files: List<File>,
        listener: ProgressListener
    ) = scope.launch(Dispatchers.IO) {
        try {
            var totalLength = 0L
            val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
            for (file in files) {
                val contentType: String =
                    URLConnection.getFileNameMap().getContentTypeFor(file.name)
                builder.addFormDataPart(
                    "file",
                    file.name,
                    RequestBody.create(MediaType.parse(contentType), file)
                )
                totalLength += file.length()
            }
            val multipartBody = builder.build()
            val requestBody = if (totalLength > 1024 * 1024) {//大于1M显示进度
                ProgressRequestBody(multipartBody) { percent ->
                    scope.launch(Dispatchers.Main) {
                        listener.onProgress(percent, totalLength)
                    }
                }
            } else {
                multipartBody
            }
            val response = apiService.uploadFile(requestBody)
            withContext(Dispatchers.Main) {
                if (response.code == 10000) {
                    listener.onResult(totalLength, "上传成功")
                } else {
                    listener.onResult(null, "上传失败")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                listener.onResult(null, getMessage(e) ?: "上传失败")
            }
        }
    }

    interface ProgressListener {
        /**
         * 大于1M显示进度
         * @param percent 进度百分比 "%{percent}%"
         * @param total 文件总大小（单位B）
         */
        fun onProgress(percent: Long, total: Long)

        /**
         * 结果
         * @param total 文件大小（单位B） null表示失败，非null表示成功
         * @param message 结果提示信息
         */
        fun onResult(total: Long?, message: String)
    }
}