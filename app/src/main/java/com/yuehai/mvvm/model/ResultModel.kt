package com.yuehai.mvvm.model

open class ResultModel<T>(private val code: Int?, private val msg: String?) {
    fun isOK() = code == 200
    fun message(default: String = "请求失败") = if (msg != null && msg.length < 35) msg else default
    open fun data(): T? = null
}

class SuccessModel<T>(code: Int, msg: String, val data: T?) : ResultModel<T>(code, msg) {
    override fun data() = data
}

class SuccessPagesModel<T>(code: Int, msg: String, val rows: T?) : ResultModel<T>(code, msg) {
    override fun data() = rows
}

class ErrorModel<T>(msg: String?) : ResultModel<T>(null, msg)