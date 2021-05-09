package com.yuehai.mvvm.network

import com.yuehai.mvvm.model.ErrorModel
import com.yuehai.mvvm.model.SuccessModel
import kotlinx.coroutines.*

/**
 * Created by zhaoyuehai 2021/4/27
 */
interface RequestCallback<T> {
    fun onSuccess(result: SuccessModel<T>)
    fun onError(error: ErrorModel)
}

fun <T> request(
    scope: CoroutineScope,
    callback: RequestCallback<T>? = null,
    block: suspend CoroutineScope.() -> SuccessModel<T>
): Job = scope.launch(Dispatchers.IO) {
    try {
        val result = block()
        withContext(Dispatchers.Main) {
            callback?.onSuccess(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            callback?.onError(ErrorModel(e))
        }
    }
}