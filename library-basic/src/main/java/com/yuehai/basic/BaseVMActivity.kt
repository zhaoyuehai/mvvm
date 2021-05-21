package com.yuehai.basic

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yuehai.util.DialogUtil
import com.yuehai.util.StatusBarUtil
import com.yuehai.widget.CustomToolbar

/**
 * Created by zhaoyuehai 2021/4/25
 */
abstract class BaseVMActivity<DB : ViewDataBinding, VM : BaseViewModel> : BaseActivity() {
    @get:LayoutRes
    protected abstract val layout: Int

    @get:IdRes
    protected abstract val variableId: Int

    protected abstract val viewModelClass: Class<VM>

    protected var viewDataBinding: DB? = null

    protected lateinit var viewModel: VM

    protected open val translucentStatusBarEnable = false

    protected open fun getCustomToolbar(): CustomToolbar? = null

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(viewModelClass)
        viewDataBinding = DataBindingUtil.setContentView(this, layout)
        viewDataBinding?.apply {
            lifecycleOwner = this@BaseVMActivity
            setVariable(variableId, viewModel)
        }
        viewModel.init(savedInstanceState, intent.extras)
        if (translucentStatusBarEnable) {
            StatusBarUtil.setStatusBarDarkTheme(this, true)
            StatusBarUtil.setTranslucentStatus(this)
            StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        }
        getCustomToolbar()?.let { toolbar ->
            toolbar.navButtonView.setOnClickListener {
                onToolbarBack()
            }
        }
        init(savedInstanceState)
    }

    open fun init(savedInstanceState: Bundle?) {
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

    protected open fun onToolbarBack() = finish()

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.unbind()
    }

    protected fun hideSoftInput() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            window.decorView.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    protected fun showSoftInput(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    protected fun showToast(msg: String) {
        if (msg.isNotEmpty())
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}