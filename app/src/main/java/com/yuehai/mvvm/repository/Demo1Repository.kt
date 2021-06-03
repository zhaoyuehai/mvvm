package com.yuehai.mvvm.repository

import com.yuehai.basic.BaseRepository
import com.yuehai.mvvm.model.ResultModel
import com.yuehai.mvvm.model.SuccessModel
import com.yuehai.mvvm.network.HttpClient
import com.yuehai.mvvm.network.request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * Created by zhaoyuehai 2021/4/27
 */
class Demo1Repository(scope: CoroutineScope) : BaseRepository(scope) {

    fun loadData1(callback: (ResultModel<String>) -> Unit) {
        request(scope, callback) {
            HttpClient.service.test()
        }
    }

    fun loadData2(callback: (ResultModel<String>) -> Unit) {
        request(scope, callback) {
            load()
        }
    }

    private suspend fun load(): SuccessModel<String> {
        var result: String? = null
        withContext(Dispatchers.IO) {
            delay(1500)
            try {
                val url = URL("https://www.baidu.com")
                val connection: URLConnection = url.openConnection()
                connection.connect()
                val isr = InputStreamReader(connection.getInputStream())
                val br = BufferedReader(isr)
                var str: String?
                val sb = StringBuilder()
                while (br.readLine().also { str = it } != null) {
                    sb.append(str)
                }
                result = sb.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return SuccessModel(200, "请求成功", result)
    }
}