package com.yuehai.mvvm.vm

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel

/**
 * Created by zhaoyuehai 2021/4/28
 */
class Demo2ViewModel : BaseViewModel() {
    val title = MutableLiveData("")
    override fun init(savedInstanceState: Bundle?, arguments: Bundle?) {
        super.init(savedInstanceState, arguments)
        title. value=arguments?.getString("title") ?: ""
    }
}