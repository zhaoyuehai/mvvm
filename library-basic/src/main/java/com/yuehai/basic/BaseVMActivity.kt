package com.yuehai.basic

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KClass

/**
 * Created by zhaoyuehai 2021/4/25
 */
abstract class BaseVMActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes val contentLayoutId: Int,
    @IdRes val variableId: Int,
    viewModelKClass: KClass<VM>
) : BaseActivity() {
    protected lateinit var binding: VB
    val viewModel by viewModel(viewModelKClass)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, contentLayoutId)
        binding.lifecycleOwner = this
        viewModel.initParams(savedInstanceState, intent.extras)
        binding.setVariable(variableId, viewModel)
        initView(savedInstanceState)
        viewModel.initData()
    }

    /**
     * ① ViewModel.initParams(...) → ② Activity.initView(.) → ③ ViewModel.initData()
     *
     * @param savedInstanceState Activity → onCreate(savedInstanceState)
     */
    protected open fun initView(savedInstanceState: Bundle?) {
        viewModel.finish.observe(this, {
            if (it != null) {
                if (it) setResult(RESULT_OK)
                finish()
            }
        })
        viewModel.toast.observe(this, {
            showToast(it)
        })
        viewModel.showLoading.observe(this, {
            if (it == null) {
                dismissLoading()
            } else {
                showLoading(it)
            }
        })
    }
}