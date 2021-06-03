package com.yuehai.basic

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.yuehai.widget.MyProgressDialog
import com.yuehai.widget.ProgressDialogUtil

/**
 * Created by zhaoyuehai 2021/4/27
 */
abstract class BaseFragment @JvmOverloads constructor(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {
    private var loadingDialog: MyProgressDialog? = null

    /**
     * 弹出加载中dialog
     */
    protected fun showLoading(msg: String = "") {
        activity?.let {
            loadingDialog = if (it is BaseActivity)
                it.getLoadingDialog()
            else
                ProgressDialogUtil.getProgressDialog(it)
            loadingDialog?.show(msg)
        }
    }

    protected fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        loadingDialog = null
        super.onDestroy()
    }
}

fun <VB : ViewBinding> Fragment.binding(bind: (View) -> VB) = lazy {
    bind(this.requireView()).also {
        if (it is ViewDataBinding) {
            it.lifecycleOwner = this.viewLifecycleOwner
            this.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    it.unbind()
                }
            })
        }
    }
}

fun BaseFragment.showToast(msg: String?) = activity?.let {
    if (msg.isNullOrEmpty()) return@let
    if (it is BaseActivity) {
        it.showToast(msg)
    } else {
        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
    }
}