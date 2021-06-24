package com.yuehai.mvvm.vm

import com.yuehai.basic.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class MapViewModel @Inject constructor(): BaseViewModel() {
    val title = "高德地图Demo"
}