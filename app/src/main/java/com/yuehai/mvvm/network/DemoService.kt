package com.yuehai.mvvm.network

import com.yuehai.mvvm.model.SuccessModel
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by zhaoyuehai 2021/4/27
 */
interface DemoService {
    @GET("test")
    suspend fun test(): SuccessModel<String>
}