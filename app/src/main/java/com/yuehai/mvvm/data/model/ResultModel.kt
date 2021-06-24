package com.yuehai.mvvm.data.model

open class ResultModel<out T : Any>(val code: Int?, private val msg: String?) {
    fun isOK() = code == 200
    fun message(default: String = "请求失败") = if (msg != null && msg.length < 35) msg else default
    open fun data(): T? = null
}