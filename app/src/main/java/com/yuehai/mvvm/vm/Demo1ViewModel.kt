package com.yuehai.mvvm.vm

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuehai.basic.BaseViewModel
import com.yuehai.mvvm.repository.Demo1Repository
import com.yuehai.util.SingleLiveEvent

/**
 * Created by zhaoyuehai 2021/4/28
 */
class Demo1ViewModel : BaseViewModel() {
    private val repository = Demo1Repository(viewModelScope)
    val bottomDialog = SingleLiveEvent<Pair<String, ((positive: Boolean) -> Unit)>?>()
    val testData = MutableLiveData<Spanned?>()
    val testBtn = MutableLiveData("点我关闭页面")

    fun loadData() {
        showLoading("加载中...")
//        repository.loadData1 {
//            Log.d("Demo1",it.data()?:"")
//        }
        repository.loadData2 {
            dismissLoading()
            if (it.isOK()) {
                showToast(it.message("请求成功了🤩"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    testData.value = Html.fromHtml(it.data(), Html.FROM_HTML_MODE_LEGACY)
                } else {
                    testData.value = SpannableString(it.data())
                }
            } else {
                showToast(it.message("请求失败是😂"))
            }
        }
    }

    fun showBottomDialog() {
        bottomDialog.value = Pair("你确定要删除吗？") {
            testData.value = null
            toast.value = "点了确定"
        }
    }
}