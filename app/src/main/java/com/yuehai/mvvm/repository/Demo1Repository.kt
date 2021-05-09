package com.yuehai.mvvm.repository

import com.yuehai.mvvm.model.SuccessModel
import com.yuehai.basic.BaseRepository
import com.yuehai.mvvm.network.RequestCallback
import com.yuehai.mvvm.network.request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    fun loadData(
        callback: RequestCallback<String>
    ) {
        request(scope, callback) {
            //TODO Retrofit封装
//            Retrofit.Builder()
//                .baseUrl(BuildConfig.BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(XXXService::class.java)
//                .loadData()

            load()//TODO 此处假装请求一下
        }
    }

    private suspend fun load(): SuccessModel<String> {
        var result: String? = null
        withContext(Dispatchers.IO) {
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
        return SuccessModel("10000", "请求成功", "1.0.0", result)
    }
}