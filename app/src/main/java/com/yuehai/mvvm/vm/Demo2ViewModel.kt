package com.yuehai.mvvm.vm

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class Demo2ViewModel @Inject constructor() : BaseViewModel() {
    val title = MutableLiveData("")
    override fun initParams(savedInstanceState: Bundle?, arguments: Bundle?) {
        super.initParams(savedInstanceState, arguments)
        title.value = arguments?.getString("title") ?: ""
    }
}