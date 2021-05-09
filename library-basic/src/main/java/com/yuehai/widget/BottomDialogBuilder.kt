package com.yuehai.widget

import android.content.Context
import com.yuehai.widget.BottomDialog.DialogCallback
import com.yuehai.widget.BottomDialog.DismissCallback

class BottomDialogBuilder(private val context: Context) {
    private val dialog: BottomDialog = BottomDialog(context)

    /**
     * @param title 自定义样式标题
     * @return builder
     */
    fun title(title: CharSequence?): BottomDialogBuilder {
        dialog.setTitleText(title!!)
        return this
    }

    /**
     * @param title 自定义样式标题
     * @return builder
     */
    fun title(title: Int): BottomDialogBuilder {
        dialog.setTitleText(context.resources.getString(title))
        return this
    }

    /**
     * @param callback 回调
     * @return builder
     */
    fun callback(callback: DialogCallback?): BottomDialogBuilder {
        dialog.setCallback(callback)
        return this
    }

    /**
     * @param callback 回调
     * @return builder
     */
    fun dismissCallback(callback: DismissCallback?): BottomDialogBuilder {
        dialog.setDismissCallback(callback)
        return this
    }

    /**
     * @param autoDismiss 是否点击按钮后自动取消和隐藏对话框,设为true后无需在callback中处理对话框的隐藏和显示
     * @return builder
     */
    fun autoDismiss(autoDismiss: Boolean): BottomDialogBuilder {
        dialog.setAutoDismiss(autoDismiss)
        return this
    }

    /**
     * @param leftButton  左边按钮 对应取消
     * @param rightButton 右边按钮 对应确定
     * @return builder
     */
    fun buttons(leftButton: CharSequence?, rightButton: CharSequence?): BottomDialogBuilder {
        dialog.setButton(leftButton!!, rightButton!!)
        return this
    }

    /**
     * @param leftButton  左边按钮 对应取消
     * @param rightButton 右边按钮 对应确定
     * @return builder
     */
    fun buttons(leftButton: Int, rightButton: Int): BottomDialogBuilder {
        dialog.setButton(leftButton, rightButton)
        return this
    }

    fun build(): BottomDialog {
        return dialog
    }

    fun buildAndShow(): BottomDialog {
        dialog.show()
        return dialog
    }

}