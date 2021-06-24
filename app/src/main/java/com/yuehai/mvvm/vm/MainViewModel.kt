package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel
import com.yuehai.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * Created by zhaoyuehai 2021/4/25
 */
@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel() {
    val toPage = SingleLiveEvent<Int>()
    val test = MutableLiveData("测试")
    fun toPage(index: Int) {
        toPage.value = index
    }
}