package com.yuehai.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import com.yuehai.mvvm.R

class BottomDialog(context: Context) : Dialog(
    context, R.style.MyDialog
) {
    private var mBtnDialogRight: TextView? = null
    private var mBtnDialogLeft: TextView? = null
    private var callback: DialogCallback? = null
    private var dismissCallback: DismissCallback? = null
    private var mAutoDismiss = true
    private var titleText: CharSequence = ""
    private var leftButtonText: CharSequence = "取消"
    private var rightButtonText: CharSequence = "确定"
    fun setAutoDismiss(mAutoDismiss: Boolean) {
        this.mAutoDismiss = mAutoDismiss
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.decorView.setPadding(0, 0, 0, 0)
            val params = window.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.BOTTOM
            window.attributes = params
            onWindowAttributesChanged(params)
        }
        val mDialogView = View.inflate(context, R.layout.layout_my_dialog, null)
        mBtnDialogLeft = mDialogView.findViewById(R.id.btn_dialog_cancel)
        mBtnDialogRight = mDialogView.findViewById(R.id.btn_dialog_confirm)
        val mTvTitle = mDialogView.findViewById<TextView>(R.id.tv_title)
        initEvent()
        if (TextUtils.isEmpty(titleText)) {
            mTvTitle.text = ""
        } else {
            mTvTitle.text = titleText
        }
        mBtnDialogLeft?.text = leftButtonText
        mBtnDialogRight?.text = rightButtonText
        setContentView(mDialogView)
        if (dismissCallback != null) {
            setOnDismissListener {
                dismissCallback!!.dismissCallback(
                    this@BottomDialog
                )
            }
        }
    }

    private fun initEvent() {
        mBtnDialogRight!!.setOnClickListener {
            if (callback != null) {
                callback!!.confirmCallback(this@BottomDialog)
            }
            if (mAutoDismiss) {
                dismiss()
            }
        }
        mBtnDialogLeft!!.setOnClickListener {
            if (callback != null) {
                callback!!.cancelCallback(this@BottomDialog)
            }
            if (mAutoDismiss) {
                dismiss()
            }
        }
    }

    fun setTitleText(titleText: CharSequence) {
        this.titleText = titleText
    }

    fun setCallback(callback: DialogCallback?) {
        this.callback = callback
    }

    fun setDismissCallback(callback: DismissCallback?) {
        dismissCallback = callback
    }

    /**
     * @param leftButton  显示在取消位置的按钮文字
     * @param rightButton 显示在确认位置的按钮文字
     */
    fun setButton(leftButton: CharSequence, rightButton: CharSequence) {
        leftButtonText = leftButton
        rightButtonText = rightButton
    }

    /**
     * @param leftButton  显示在取消位置的按钮文字
     * @param rightButton 显示在确认位置的按钮文字
     */
    fun setButton(leftButton: Int, rightButton: Int) {
        leftButtonText = context.resources.getString(leftButton)
        rightButtonText = context.resources.getString(rightButton)
    }

    interface DialogCallback {
        fun confirmCallback(dialog: BottomDialog?)
        fun cancelCallback(dialog: BottomDialog?)
    }

    interface DismissCallback {
        fun dismissCallback(dialog: BottomDialog?)
    }
}