package com.yuehai.mvvm.data

import com.google.gson.stream.MalformedJsonException
import com.yuehai.mvvm.data.model.ErrorModel
import com.yuehai.mvvm.data.model.ResultModel
import com.yuehai.mvvm.data.model.TestData
import com.yuehai.mvvm.data.remote.ApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by zhaoyuehai 2021/4/27
 */
@Singleton
open class DataRepository @Inject constructor(private val apiService: ApiService) {

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
    ): Job = scope.launch(Dispatchers.IO) {
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
}