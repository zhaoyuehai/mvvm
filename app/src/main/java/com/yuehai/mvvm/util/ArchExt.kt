package com.yuehai.mvvm.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.yuehai.mvvm.R
import com.yuehai.widget.BottomDialog
import com.yuehai.widget.BottomDialogBuilder

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { it?.let { t -> action(t) } })
}

fun Activity.showBottomDialog(pair: Pair<String, (positive: Boolean) -> Unit>) {
    showBottomDialog(
        this, pair.first
    ) { _, which ->
        pair.second.invoke(which == DialogInterface.BUTTON_POSITIVE)
    }
}

fun Fragment.showBottomDialog(pair: Pair<String, (positive: Boolean) -> Unit>?) {
    if (pair != null) {
        activity?.let { activity ->
            showBottomDialog(
                activity, pair.first
            ) { _, which ->
                pair.second.invoke(which == DialogInterface.BUTTON_POSITIVE)
            }
        }
    }
}

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
