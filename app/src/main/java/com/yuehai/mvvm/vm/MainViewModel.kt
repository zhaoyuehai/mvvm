package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel
import com.yuehai.util.SingleLiveEvent


/**
 * Created by zhaoyuehai 2021/4/25
 */
class MainViewModel : BaseViewModel() {
    val toPage = SingleLiveEvent<Int>()
    val test = MutableLiveData("测试")
    fun toPage(index: Int) {
        toPage.value = index
    }
}