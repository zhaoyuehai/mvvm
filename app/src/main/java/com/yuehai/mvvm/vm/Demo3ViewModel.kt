package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel

/**
 * Created by zhaoyuehai 2021/4/28
 */
class Demo3ViewModel : BaseViewModel() {
    val title = MutableLiveData("这个Demo3Fragment.kt类")
}