package com.yuehai.basic

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yuehai.util.DialogUtil

/**
 * Created by zhaoyuehai 2021/4/25
 */
open class BaseVMFragment<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes contentLayoutId: Int,
    bind: (View) -> VB,
    @IdRes val variableId: Int,
    viewModelKClass: Class<VM>
) : BaseFragment(contentLayoutId) {
    protected val binding by binding(bind)
    val viewModel by lazy { ViewModelProvider(this).get(viewModelKClass) }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initParams(savedInstanceState, arguments)
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
        viewModel.bottomDialog.observe(this, {
            if (it != null) {
                activity?.let { activity ->
                    DialogUtil.showBottomDialog(
                        activity, it.first
                    ) { _, which ->
                        it.second.invoke(which == DialogInterface.BUTTON_POSITIVE)
                    }
                }
            }
        })
    }
}