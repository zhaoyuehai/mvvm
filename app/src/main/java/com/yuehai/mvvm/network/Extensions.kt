package com.yuehai.mvvm.network

import com.google.gson.stream.MalformedJsonException
import com.yuehai.mvvm.model.ErrorModel
import com.yuehai.mvvm.model.ResultModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by zhaoyuehai 2021/4/27
 */
fun <T> request(
    scope: CoroutineScope,
    callback: (ResultModel<T>) -> Unit,
    block: suspend CoroutineScope.() -> ResultModel<T>
): Job = scope.launch(Dispatchers.IO) {
    try {
        val result = block()
        withContext(Dispatchers.Main) {
            callback.invoke(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            callback.invoke(ErrorModel(getErrorMessage(e)))
        }
    }
}

fun getErrorMessage(e: Exception?) = when (e) {
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
    else -> e?.message
}
