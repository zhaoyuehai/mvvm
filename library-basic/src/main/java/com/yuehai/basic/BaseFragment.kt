package com.yuehai.basic

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlin.reflect.KClass

abstract class BaseFragment @JvmOverloads constructor(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId)

inline fun <reified A : FragmentActivity> BaseFragment.castActivity() = activity?.let {
    if (it is A) it else null
}

fun BaseFragment.showLoading(msg: String = "") = castActivity<BaseActivity>()?.showLoading(msg)

fun BaseFragment.dismissLoading() = castActivity<BaseActivity>()?.dismissLoading()

fun BaseFragment.showToast(msg: String) = castActivity<BaseActivity>()?.showToast(msg)

fun <VM : ViewModel> BaseFragment.viewModel(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore = { viewModelStore },
    factoryProducer: (() -> ViewModelProvider.Factory) = { defaultViewModelProviderFactory }
) = ViewModelLazy(viewModelClass, storeProducer, factoryProducer)

fun <VM : ViewModel> BaseFragment.activityViewModel(
    viewModelClass: KClass<VM>,
    factoryProducer: (() -> ViewModelProvider.Factory) = { defaultViewModelProviderFactory }
) = viewModel(viewModelClass, { requireActivity().viewModelStore }, factoryProducer)