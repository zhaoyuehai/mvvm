package com.yuehai.widget

import android.content.Context
import android.content.DialogInterface

/**
 * Created by zhaoyuehai 2019/3/19
 */
object ProgressDialogUtil {
    /**
     * 弹出进度对话框
     *
     * @param context Context
     * @return Dialog
     */
    fun getProgressDialog(context: Context): MyProgressDialog {
        //默认触摸不消失，返回键消失
        return getProgressDialog(context, cancelable = true, cancelTouch = false)
    }

    /**
     * 弹出进度对话框
     *
     * @param context     Context
     * @param text        文字提示
     * @param cancelable  按返回键是否取消
     * @param cancelTouch 触摸是否取消
     * @return Dialog
     */
    fun getProgressDialog(
        context: Context,
        cancelable: Boolean,
        cancelTouch: Boolean
    ): MyProgressDialog {
        val progress = MyProgressDialog(context)
        progress.setCancelable(cancelable)
        progress.setCanceledOnTouchOutside(cancelTouch)
        return progress
    }

    /**
     * 取消对话框
     *
     * @param dialog DialogInterface
     */
    fun dismissDialog(dialog: DialogInterface?) {
        dialog?.dismiss()
    }
}
