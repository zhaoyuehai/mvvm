package com.yuehai.basic

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

abstract class BaseFragment @JvmOverloads constructor(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId)

inline fun <reified A : FragmentActivity> BaseFragment.castActivity() = activity?.let {
    if (it is A) it else null
}

fun <VB : ViewDataBinding> BaseFragment.binding(bind: (View) -> VB) = lazy {
    bind(this.requireView()).also {
        it.lifecycleOwner = this.viewLifecycleOwner
        this.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                it.unbind()
            }
        })
    }
}

fun BaseFragment.showLoading(msg: String = "") = castActivity<BaseActivity>()?.showLoading(msg)

fun BaseFragment.dismissLoading() = castActivity<BaseActivity>()?.dismissLoading()

fun BaseFragment.showToast(msg: String) = castActivity<BaseActivity>()?.showToast(msg)