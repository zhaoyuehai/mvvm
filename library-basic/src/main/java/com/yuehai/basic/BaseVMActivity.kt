package com.yuehai.basic

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yuehai.util.DialogUtil
import com.yuehai.widget.MyProgressDialog
import com.yuehai.widget.ProgressDialogUtil

/**
 * Created by zhaoyuehai 2021/4/25
 */
abstract class BaseVMActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    inflate: (LayoutInflater) -> VB,
    @IdRes val variableId: Int,
    viewModelKClass: Class<VM>
) : BaseActivity() {
    protected val binding by lazy { binding(inflate) }
    val viewModel by lazy { ViewModelProvider(this).get(viewModelKClass) }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewModel.bottomDialog.observe(this, {
            if (it != null) {
                DialogUtil.showBottomDialog(
                    this, it.first
                ) { _, which ->
                    it.second.invoke(which == DialogInterface.BUTTON_POSITIVE)
                }
            }
        })
    }
}