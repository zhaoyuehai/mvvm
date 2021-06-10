package com.yuehai.basic

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KClass

/**
 * Created by zhaoyuehai 2021/4/25
 */
open class BaseVMFragment<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes val contentLayoutId: Int,
    @IdRes val variableId: Int,
    viewModelKClass: KClass<VM>
) : BaseFragment(contentLayoutId) {
    protected lateinit var binding: VB
    val viewModel by viewModel(viewModelKClass)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initParams(savedInstanceState, arguments)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(variableId, viewModel)
        initView(savedInstanceState)
        viewModel.initData()
    }

    /**
     * ① ViewModel.initParams(...) → ② Fragment.initView(.) → ③ ViewModel.initData()
     *
     * @param savedInstanceState Activity → onCreate(savedInstanceState) / Fragment → onViewCreated(view, savedInstanceState)
     */
    protected open fun initView(savedInstanceState: Bundle?) {
        viewModel.finish.observe(this, {
            if (it != null) {
                if (it) activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
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