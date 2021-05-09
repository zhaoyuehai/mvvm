package com.yuehai.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import com.yuehai.basic.R
import com.yuehai.widget.BottomDialog
import com.yuehai.widget.BottomDialogBuilder

/**
 * Created by zhaoyuehai 2021/5/7
 */
object DialogUtil {

    fun showBottomDialog(
        context: Context,
        title: String,
        onClickListener: DialogInterface.OnClickListener
    ) = showBottomDialog(context, title, R.string.confirm, R.string.cancel, onClickListener, true)

    fun showBottomDialog(
        context: Context,
        title: String,
        @StringRes positiveTextId: Int,
        @StringRes negativeTextId: Int,
        onClickListener: DialogInterface.OnClickListener,
        isAutoDismiss: Boolean
    ): Dialog {
        val builder = BottomDialogBuilder(context)
        builder.title(title)
        builder.autoDismiss(isAutoDismiss)
        val dialog: Dialog = builder
            .buttons(negativeTextId, positiveTextId)
            .callback(object : BottomDialog.DialogCallback {
                override fun confirmCallback(dialog: BottomDialog?) {
                    onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
                }

                override fun cancelCallback(dialog: BottomDialog?) {
                    onClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
                }

            })
            .build()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        return dialog
    }

}