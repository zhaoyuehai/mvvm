package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.yuehai.basic.BaseViewModel
import com.yuehai.mvvm.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class Demo3ViewModel @Inject constructor(private val dataRepository: DataRepository) :
    BaseViewModel() {
    val title = MutableLiveData("这个Demo3Fragment.kt类")
}