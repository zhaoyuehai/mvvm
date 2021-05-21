package com.yuehai.mvvm.network

import com.google.gson.stream.MalformedJsonException
import com.yuehai.mvvm.model.ErrorModel
import com.yuehai.mvvm.model.ResultModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by zhaoyuehai 2021/4/27
 */
fun <T> request(
    scope: CoroutineScope,
    callback: (ResultModel<T>) -> Unit,
    block: suspend CoroutineScope.() -> ResultModel<T>
): Job = scope.launch(Dispatchers.IO) {
    try {
        val result = block()
        withContext(Dispatchers.Main) {
            callback.invoke(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            callback.invoke(ErrorModel(getErrorMessage(e)))
        }
    }
}

fun getErrorMessage(e: Exception?) = when (e) {
    is HttpException -> "�����������쳣"//404 500 ���������Ӵ���
    is IOException -> {
        when (e) {
            is UnknownHostException -> "����ʧ�ܣ������ԣ�"
            is SocketTimeoutException -> "����ʱ"
            //�����������ӳ�ʱ����Ӧ
            is SocketException -> {
                when {
                    e is ConnectException -> "���������쳣" //������
                    e.message == "Socket closed" -> "������ȡ��"
                    else -> e.message
                }
            }
            is MalformedJsonException -> "���ݽ���ʧ��"//gson�����쳣
            else -> if (e.message == "Canceled") "������ȡ��" else e.message
        }
    }
    else -> e?.message
}
