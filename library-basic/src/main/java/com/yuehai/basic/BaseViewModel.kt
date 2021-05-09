package com.yuehai.basic

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.yuehai.util.SingleLiveEvent

/**
 * Created by zhaoyuehai 2021/4/26
 */
abstract class BaseViewModel : ViewModel() {
    val toast = SingleLiveEvent<String>()
    val bottomDialog = SingleLiveEvent<Pair<String, ((positive: Boolean) -> Unit)>?>()
    val showLoading = SingleLiveEvent<String?>()
    val finish = SingleLiveEvent<Boolean?>()
    open fun init(savedInstanceState: Bundle? = null) {}
}