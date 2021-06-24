package com.yuehai.mvvm.data.remote

import com.yuehai.mvvm.data.model.SuccessModel
import com.yuehai.mvvm.data.model.TestData
import retrofit2.http.GET

/**
 * Created by zhaoyuehai 2021/4/27
 */
interface ApiService {
    @GET("test")
    suspend fun testData(): SuccessModel<TestData>

    @GET("testList")
    suspend fun testListData(): SuccessModel<List<TestData>>
}