package com.yuehai.mvvm.vm

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuehai.mvvm.model.ErrorModel
import com.yuehai.mvvm.model.SuccessModel
import com.yuehai.basic.BaseViewModel
import com.yuehai.mvvm.network.RequestCallback
import com.yuehai.mvvm.repository.Demo1Repository
import com.yuehai.util.SingleLiveEvent

/**
 * Created by zhaoyuehai 2021/4/28
 */
class Demo1ViewModel : BaseViewModel() {
    private val repository = Demo1Repository(viewModelScope)

    val testData = MutableLiveData<Spanned?>()
    val testBtn = MutableLiveData("点我关闭页面")

    val toPage = SingleLiveEvent<Boolean>()

    fun clickText() {
        finish.value = true// true ：setResultOK
    }

    fun loadData() {
        showLoading.value = ""
        repository.loadData(object : RequestCallback<String> {
            override fun onSuccess(result: SuccessModel<String>) {
                showLoading.value = null
                toast.value = result.message
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    testData.value = Html.fromHtml(result.data, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    testData.value = SpannableString(result.data)
                }
            }

            override fun onError(error: ErrorModel) {
                showLoading.value = null
                toast.value = error.message() ?: "请求失败"
            }

        })
    }

    fun showBottomDialog() {
        bottomDialog.value = Pair("你确定要删除吗？") {
            testData.value = null
            toast.value = "点了确定"
        }
    }

    fun clickToPage() {
        toPage.value = true
    }
}