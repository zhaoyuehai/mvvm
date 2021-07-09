package com.yuehai.mvvm.data.remote

import com.yuehai.mvvm.data.model.SuccessModel
import com.yuehai.mvvm.data.model.TestData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by zhaoyuehai 2021/4/27
 */
interface ApiService {
    @Streaming
    @GET
    suspend fun download(@Url url: String): Response<ResponseBody>

    /**
     * 文件上传【测试接口】
     */
    @Headers("authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxODg4ODg4ODg4OCIsImdlbnRfdHlwZSI6ImFjY2Vzc1Rva2VuIiwiZXhwIjoxNjI1ODI2NTQ2fQ.JuPxMwEVRkAhoMxErG7nYDA7VrfH-Vr7i4pSDD91FHc")
    @POST("api/v1/file/upload")
    suspend fun uploadFile(@Body file: RequestBody): SuccessModel<String>

    @GET("api/v1/test")
    suspend fun testData(): SuccessModel<TestData>

    @GET("api/v1/testList")
    suspend fun testListData(): SuccessModel<List<TestData>>
}