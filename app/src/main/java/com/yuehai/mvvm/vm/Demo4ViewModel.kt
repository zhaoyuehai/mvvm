package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class Demo4ViewModel @Inject constructor(): BaseViewModel() {
    val title = MutableLiveData("这个匿名BaseVMFragment.kt类")
}