package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuehai.basic.BaseViewModel
import com.yuehai.mvvm.data.DataRepository
import com.yuehai.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class Demo1ViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    val bottomDialog = SingleLiveEvent<Pair<String, ((positive: Boolean) -> Unit)>?>()
    val testData = MutableLiveData<String>()
    val testBtn = MutableLiveData("点我关闭页面")

    fun loadData() {
        showLoading("加载中...")
        repository.loadData(viewModelScope) {
            dismissLoading()
            if (it.isOK()) {
                showToast(it.message("请求成功了🤩"))
                testData.value = it.data().toString()
            } else {
                showToast(it.message("请求失败是😂"))
            }
        }
    }

    fun showBottomDialog() {
        bottomDialog.value = Pair("你确定要删除吗？") {
            testData.value = ""
            toast.value = "点了确定"
        }
    }
}