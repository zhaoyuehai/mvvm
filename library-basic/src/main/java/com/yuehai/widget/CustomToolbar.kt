package com.yuehai.widget

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.yuehai.basic.R

/**
 * Created by zhaoyuehai 2021/4/29
 */
class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val navButtonView: ImageButton
    var menuButtonView: ImageButton? = null
    private val mTitleTextView: TextView?
    fun setTitle(title: String?) {
        if (mTitleTextView != null) {
            mTitleTextView.text = title
        }
    }

    init {
        val dp50 = (50 * context.resources.displayMetrics.density).toInt()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar)
        typedArray.getDrawable(R.styleable.CustomToolbar_textColor)
        mTitleTextView = AppCompatTextView(context)
        mTitleTextView.setSingleLine()
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END)
        val mTitleTextColor = typedArray.getColorStateList(R.styleable.CustomToolbar_textColor)
        if (mTitleTextColor != null) {
            mTitleTextView.setTextColor(mTitleTextColor)
        } else {
            mTitleTextView.setTextColor(Color.WHITE)
        }
        mTitleTextView.setTextSize(18f)
        mTitleTextView.setGravity(Gravity.CENTER)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            leftMargin = dp50
            rightMargin = dp50
            gravity = Gravity.CENTER_VERTICAL
        }
        mTitleTextView.setLayoutParams(params)
        val title = typedArray.getString(R.styleable.CustomToolbar_title)
        if (title != null) {
            mTitleTextView.setText(title)
        }
        addView(mTitleTextView)
        navButtonView = AppCompatImageButton(context)
        navButtonView.setLayoutParams(LayoutParams(dp50, dp50).apply {
            gravity = Gravity.CENTER_VERTICAL
        })
        navButtonView.setBackgroundResource(R.drawable.control_background_40dp_material)
        val icon = typedArray.getDrawable(R.styleable.CustomToolbar_icon)
        if (icon != null) {
            navButtonView.setImageDrawable(icon)
        } else {
            navButtonView.setImageResource(R.drawable.ic_back_white)
        }
        addView(navButtonView)
        val rightIcon = typedArray.getDrawable(R.styleable.CustomToolbar_rightIcon)
        if (rightIcon != null) {
            menuButtonView = AppCompatImageButton(context).apply {
                setImageDrawable(rightIcon)
                layoutParams = LayoutParams(dp50, dp50).apply {
                    gravity = Gravity.CENTER_VERTICAL or Gravity.END
                }
                setBackgroundResource(R.drawable.control_background_40dp_material)
                addView(this)
            }
        }
        typedArray.recycle()
    }
}

@BindingAdapter("onBackClickListener")
fun setOnBackClickListener(itemView: CustomToolbar, onBackClickListener: View.OnClickListener?) {
    itemView.navButtonView.setOnClickListener(onBackClickListener)
}

@BindingAdapter("onMenuClickListener")
fun setOnMenuClickListener(itemView: CustomToolbar, onMenuClickListener: View.OnClickListener?) {
    itemView.menuButtonView?.setOnClickListener(onMenuClickListener)
}