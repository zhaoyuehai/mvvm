package com.yuehai.mvvm.model

//import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ResultModel

data class SuccessModel<T>(
    val code: String,
    val message: String,
    val serviceVersion: String,
    val data: T?
) :
    ResultModel()

data class ErrorModel(val t: Throwable?) : ResultModel() {
    fun message(): String? {
        return when (t) {
//            is HttpException -> "服务器连接异常"//404 500 服务器连接错误
            is IOException -> {
                when (t) {
                    is UnknownHostException -> "请求失败，请重试！"
                    is SocketTimeoutException -> "请求超时"
                    //连上网，连接超时无响应
                    is SocketException -> {
                        when {
                            t is ConnectException -> "网络连接异常" //断网了
                            t.message == "Socket closed" -> "请求已取消"
                            else -> t.message
                        }
                    }
                    else -> if (t.message == "Canceled") "请求已取消" else t.message
                }
            }
            else -> t?.message
        }
    }
}

