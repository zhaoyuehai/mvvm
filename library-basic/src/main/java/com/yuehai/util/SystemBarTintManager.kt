/*
 * Copyright (C) 2013 readyState Software Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuehai.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout

/**
 * Class to manage status and navigation bar tint effects when using KitKat
 * translucent system UI modes.
 */
class SystemBarTintManager @SuppressLint("ResourceType") @TargetApi(19) constructor(activity: Activity) {
    /**
     * Get the system bar configuration.
     *
     * @return The system bar configuration for the current device configuration.
     */
    val config: SystemBarConfig
    private var mStatusBarAvailable = false
    private var mNavBarAvailable = false
    private var mStatusBarTintEnabled = false

    /**
     * Is tinting enabled for the system navigation bar?
     *
     * @return True if enabled, False otherwise.
     */
    var isNavBarTintEnabled = false
        private set
    private var mStatusBarTintView: View? = null
    private var mNavBarTintView: View? = null

    /**
     * Enable tinting of the system navigation bar.
     *
     *
     * If the platform does not have soft navigation keys, is running Jelly Bean
     * or earlier, or translucent system UI modes have not been enabled in either
     * the theme or via window flags, then this method does nothing.
     *
     * @param enabled True to enable tinting, false to disable it (default).
     */
    fun setNavigationBarTintEnabled(enabled: Boolean) {
        isNavBarTintEnabled = enabled
        if (mNavBarAvailable) {
            mNavBarTintView!!.visibility =
                if (enabled) View.VISIBLE else View.GONE
        }
    }

    /**
     * Apply the specified color tint to all system UI bars.
     *
     * @param color The color of the background tint.
     */
    fun setTintColor(color: Int) {
        setStatusBarTintColor(color)
        setNavigationBarTintColor(color)
    }

    /**
     * Apply the specified drawable or color resource to all system UI bars.
     *
     * @param res The identifier of the resource.
     */
    fun setTintResource(res: Int) {
        setStatusBarTintResource(res)
        setNavigationBarTintResource(res)
    }

    /**
     * Apply the specified drawable to all system UI bars.
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    fun setTintDrawable(drawable: Drawable?) {
        setStatusBarTintDrawable(drawable)
        setNavigationBarTintDrawable(drawable)
    }

    /**
     * Apply the specified alpha to all system UI bars.
     *
     * @param alpha The alpha to use
     */
    fun setTintAlpha(alpha: Float) {
        setStatusBarAlpha(alpha)
        setNavigationBarAlpha(alpha)
    }

    /**
     * Apply the specified color tint to the system status bar.
     *
     * @param color The color of the background tint.
     */
    fun setStatusBarTintColor(color: Int) {
        if (mStatusBarAvailable) {
            mStatusBarTintView!!.setBackgroundColor(color)
        }
    }

    /**
     * Apply the specified drawable or color resource to the system status bar.
     *
     * @param res The identifier of the resource.
     */
    fun setStatusBarTintResource(res: Int) {
        if (mStatusBarAvailable) {
            mStatusBarTintView!!.setBackgroundResource(res)
        }
    }

    /**
     * Apply the specified drawable to the system status bar.
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    fun setStatusBarTintDrawable(drawable: Drawable?) {
        if (mStatusBarAvailable) {
            mStatusBarTintView!!.setBackgroundDrawable(drawable)
        }
    }

    /**
     * Apply the specified alpha to the system status bar.
     *
     * @param alpha The alpha to use
     */
    @TargetApi(11)
    fun setStatusBarAlpha(alpha: Float) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView!!.alpha = alpha
        }
    }

    /**
     * Apply the specified color tint to the system navigation bar.
     *
     * @param color The color of the background tint.
     */
    fun setNavigationBarTintColor(color: Int) {
        if (mNavBarAvailable) {
            mNavBarTintView!!.setBackgroundColor(color)
        }
    }

    /**
     * Apply the specified drawable or color resource to the system navigation bar.
     *
     * @param res The identifier of the resource.
     */
    fun setNavigationBarTintResource(res: Int) {
        if (mNavBarAvailable) {
            mNavBarTintView!!.setBackgroundResource(res)
        }
    }

    /**
     * Apply the specified drawable to the system navigation bar.
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    fun setNavigationBarTintDrawable(drawable: Drawable?) {
        if (mNavBarAvailable) {
            mNavBarTintView!!.setBackgroundDrawable(drawable)
        }
    }

    /**
     * Apply the specified alpha to the system navigation bar.
     *
     * @param alpha The alpha to use
     */
    @TargetApi(11)
    fun setNavigationBarAlpha(alpha: Float) {
        if (mNavBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mNavBarTintView!!.alpha = alpha
        }
    }
    /**
     * Is tinting enabled for the system status bar?
     *
     * @return True if enabled, False otherwise.
     */
    /**
     * Enable tinting of the system status bar.
     *
     *
     * If the platform is running Jelly Bean or earlier, or translucent system
     * UI modes have not been enabled in either the theme or via window flags,
     * then this method does nothing.
     *
     * @param enabled True to enable tinting, false to disable it (default).
     */
    var isStatusBarTintEnabled: Boolean
        get() = mStatusBarTintEnabled
        set(enabled) {
            mStatusBarTintEnabled = enabled
            if (mStatusBarAvailable) {
                mStatusBarTintView!!.visibility =
                    if (enabled) View.VISIBLE else View.GONE
            }
        }

    private fun setupStatusBarView(context: Context, decorViewGroup: ViewGroup) {
        mStatusBarTintView = View(context)
        val params =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, config.statusBarHeight)
        params.gravity = Gravity.TOP
        if (mNavBarAvailable && !config.isNavigationAtBottom) {
            params.rightMargin = config.navigationBarWidth
        }
        mStatusBarTintView!!.layoutParams = params
        mStatusBarTintView!!.setBackgroundColor(SystemBarConfig.DEFAULT_TINT_COLOR)
        mStatusBarTintView!!.visibility = View.GONE
        decorViewGroup.addView(mStatusBarTintView)
    }

    private fun setupNavBarView(context: Context, decorViewGroup: ViewGroup) {
        mNavBarTintView = View(context)
        val params: FrameLayout.LayoutParams
        if (config.isNavigationAtBottom) {
            params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                config.navigationBarHeight
            )
            params.gravity = Gravity.BOTTOM
        } else {
            params = FrameLayout.LayoutParams(
                config.navigationBarWidth,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            params.gravity = Gravity.END
        }
        mNavBarTintView!!.layoutParams = params
        mNavBarTintView!!.setBackgroundColor(SystemBarConfig.DEFAULT_TINT_COLOR)
        mNavBarTintView!!.visibility = View.GONE
        decorViewGroup.addView(mNavBarTintView)
    }

    /**
     * Constructor. Call this in the host activity onCreate method after its
     * content view has been set. You should always create new instances when
     * the host activity is recreated.
     *
     * @param activity The host activity.
     */
    init {
        val win = activity.window
        val decorViewGroup = win.decorView as ViewGroup
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            val attrs = intArrayOf(
                android.R.attr.windowTranslucentStatus,
                android.R.attr.windowTranslucentNavigation
            )
            val a = activity.obtainStyledAttributes(attrs)
            try {
                mStatusBarAvailable = a.getBoolean(0, false)
                mNavBarAvailable = a.getBoolean(1, false)
            } finally {
                a.recycle()
            }

            // check window flags
            val winParams = win.attributes
            var bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (winParams.flags and bits != 0) {
                mStatusBarAvailable = true
            }
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            if (winParams.flags and bits != 0) {
                mNavBarAvailable = true
            }
        }
        config = SystemBarConfig(activity, mStatusBarAvailable, mNavBarAvailable)
        // device might not have virtual navigation keys
        if (!config.hasNavigtionBar()) {
            mNavBarAvailable = false
        }
        if (mStatusBarAvailable) {
            setupStatusBarView(activity, decorViewGroup)
        }
        if (mNavBarAvailable) {
            setupNavBarView(activity, decorViewGroup)
        }
    }
}