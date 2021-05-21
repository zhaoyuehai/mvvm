package com.yuehai.mvvm.network

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by zhaoyuehai 2021/4/27
 */
object HttpClient {
    val service: DemoService by lazy {
        Retrofit.Builder()
            .baseUrl("https://wwww.baidu.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DemoService::class.java)
    }

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addNetworkInterceptor(LoggingInterceptor())
            .connectTimeout(15000L, TimeUnit.MILLISECONDS)
//            .addInterceptor(TokenInterceptor())
        //绕过SSL验证
        val x509TrustManager = object : X509TrustManager {

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {

            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(x509TrustManager), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (ignored: Exception) {
        }
        if (ssfFactory != null) {
            builder.sslSocketFactory(ssfFactory, x509TrustManager)
                .hostnameVerifier { _, _ -> true }
        }
        builder.build()
    }

    /**
     * 取消请求
     */
    fun cancelAll() {
        okHttpClient.dispatcher().cancelAll()
    }
}