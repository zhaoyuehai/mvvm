package com.yuehai.basic

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.yuehai.widget.MyProgressDialog
import com.yuehai.widget.ProgressDialogUtil
import kotlin.reflect.KClass

/**
 * Created by zhaoyuehai 2021/4/27
 */
abstract class BaseActivity : AppCompatActivity() {
    private var loadingDialog: MyProgressDialog? = null
    fun getLoadingDialog(): MyProgressDialog {
        var dialog = loadingDialog
        if (dialog == null) {
            dialog = ProgressDialogUtil.getProgressDialog(this)
            loadingDialog = dialog
        }
        return dialog
    }

    fun showLoading(msg: String = "") {
        getLoadingDialog().show(msg)
    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    fun showToast(msg: String) {
        if (msg.isNotEmpty()) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        loadingDialog = null
        super.onDestroy()
    }
}

fun <VM : ViewModel> BaseActivity.viewModel(
    viewModelClass: KClass<VM>,
    factoryProducer: (() -> ViewModelProvider.Factory) = { defaultViewModelProviderFactory }
) = ViewModelLazy(viewModelClass, { viewModelStore }, factoryProducer)