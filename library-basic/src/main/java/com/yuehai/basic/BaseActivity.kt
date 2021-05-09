package com.yuehai.basic

import androidx.appcompat.app.AppCompatActivity
import com.yuehai.widget.MyProgressDialog
import com.yuehai.widget.ProgressDialogUtil

/**
 * Created by zhaoyuehai 2021/4/27
 */
abstract class BaseActivity : AppCompatActivity() {
    private var loadingDialog: MyProgressDialog? = null

    private var onLoadingCancelListener: (() -> Unit?)? = null

    fun setOnLoadingCancelListener(listener: () -> Unit) {
        this.onLoadingCancelListener = listener
    }

    /**
     * 弹出加载中dialog
     */
    fun showLoading(msg: String?) {
        if (null == loadingDialog) {
            loadingDialog = ProgressDialogUtil.getProgressDialog(this)
            loadingDialog?.setOnCancelListener {
                onLoadingCancelListener?.invoke()
                onLoadingCancelListener = null
            }
        }
        if (loadingDialog?.isShowing == false) {
            loadingDialog?.show()
        }
        loadingDialog?.setText(msg)
    }

    fun dismissLoading() {
        ProgressDialogUtil.dismissDialog(loadingDialog)
    }

    override fun finish() {
        dismissLoading()
        super.finish()
    }
}