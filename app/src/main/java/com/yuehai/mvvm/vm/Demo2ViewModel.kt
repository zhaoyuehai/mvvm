package com.yuehai.mvvm.vm

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel

/**
 * Created by zhaoyuehai 2021/4/28
 */
class Demo2ViewModel : BaseViewModel() {
    val title = MutableLiveData("")
    override fun initParams(savedInstanceState: Bundle?, arguments: Bundle?) {
        super.initParams(savedInstanceState, arguments)
        title.value = arguments?.getString("title") ?: ""
    }
}