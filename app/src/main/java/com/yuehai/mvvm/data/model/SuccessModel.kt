package com.yuehai.mvvm.data.model

class SuccessModel<out T : Any>(code: Int, msg: String, private val data: T?) :
    ResultModel<T>(code, msg) {
    override fun data() = data
}