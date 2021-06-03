package com.yuehai.basic

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.yuehai.widget.MyProgressDialog
import com.yuehai.widget.ProgressDialogUtil

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

    /**
     * 弹出加载中dialog
     */
    internal fun showLoading(msg: String = "") {
        getLoadingDialog().show(msg)
    }

    internal fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    internal fun showToast(msg: String) {
        if (msg.isNotEmpty())
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        loadingDialog = null
        super.onDestroy()
    }
}

fun <VB : ViewBinding> ComponentActivity.binding(inflate: (LayoutInflater) -> VB) =
    inflate(layoutInflater).also {
        setContentView(it.root)
        if (it is ViewDataBinding) {
            it.lifecycleOwner = this
            this.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    it.unbind()
                }
            })
        }
    }