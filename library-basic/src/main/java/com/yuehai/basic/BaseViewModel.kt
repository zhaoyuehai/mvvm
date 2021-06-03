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

    /**
     * ① ViewModel.initParams(...) → ② Activity/Fragment.initView(.) → ③ ViewModel.initData()
     *
     * @param savedInstanceState Activity → onCreate(savedInstanceState) / Fragment → onViewCreated(view, savedInstanceState)
     * @param arguments Activity → getIntent().getExtras() / Fragment → getArguments()
     */
    open fun initParams(savedInstanceState: Bundle?, arguments: Bundle?) {}

    /**
     * ① ViewModel.initParams(...) → ② Activity/Fragment.initView(.) → ③ ViewModel.initData()
     */
    open fun initData() {}

    fun showLoading(content: String = "") {
        showLoading.value = content
    }

    fun dismissLoading() {
        showLoading.value = null
    }

    fun showToast(msg: String) {
        toast.value = msg
    }

    fun showBottomDialog(value: Pair<String, (positive: Boolean) -> Unit>) {
        bottomDialog.value = value
    }

    @JvmOverloads
    fun finish(isResultOK: Boolean = false) {
        finish.value = isResultOK
    }
}