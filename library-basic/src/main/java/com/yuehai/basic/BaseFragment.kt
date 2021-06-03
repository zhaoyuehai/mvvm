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

/**
 * Created by zhaoyuehai 2021/4/27
 */
abstract class BaseFragment @JvmOverloads constructor(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId)

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

fun Fragment.showLoading(msg: String = "") = activity?.let {
    if (it is BaseActivity) it.showLoading(msg)
}

fun Fragment.dismissLoading() = activity?.let {
    if (it is BaseActivity) it.dismissLoading()
}

fun Fragment.showToast(msg: String?) = activity?.let {
    if (msg.isNullOrEmpty()) return@let
    if (it is BaseActivity) {
        it.showToast(msg)
    } else {
        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
    }
}