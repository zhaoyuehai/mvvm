package com.yuehai.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewConfiguration
import java.lang.reflect.Method

/**
 * Class which describes system bar sizing and other characteristics for the current
 * device configuration.
 */
class SystemBarConfig(
    activity: Activity,
    translucentStatusBar: Boolean,
    traslucentNavBar: Boolean
) {
    companion object {
        /**
         * The default system bar tint color value.
         */
        const val DEFAULT_TINT_COLOR = -0x67000000
        private var sNavBarOverride: String? = null
        private const val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
        private const val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
        private const val NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape"
        private const val NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width"
        private const val SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar"

        init {
            // Android allows a system property to override the presence of the navigation bar.
            // Used by the emulator.
            // See https://github.com/android/platform_frameworks_base/blob/master/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java#L1076
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m: Method = c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String?
                } catch (e: Throwable) {
                    sNavBarOverride = null
                }
            }
        }
    }

    private val mTranslucentStatusBar: Boolean
    private val mTranslucentNavBar: Boolean

    /**
     * Get the height of the system status bar.
     *
     * @return The height of the status bar (in pixels).
     */
    val statusBarHeight: Int

    /**
     * Get the height of the action bar.
     *
     * @return The height of the action bar (in pixels).
     */
    private val actionBarHeight: Int
    private val mHasNavigationBar: Boolean

    /**
     * Get the height of the system navigation bar.
     *
     * @return The height of the navigation bar (in pixels). If the device does not have
     * soft navigation keys, this will always return 0.
     */
    val navigationBarHeight: Int

    /**
     * Get the width of the system navigation bar when it is placed vertically on the screen.
     *
     * @return The width of the navigation bar (in pixels). If the device does not have
     * soft navigation keys, this will always return 0.
     */
    val navigationBarWidth: Int
    private val mInPortrait: Boolean
    private val mSmallestWidthDp: Float

    init {
        val res = activity.resources
        mInPortrait = res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        mSmallestWidthDp = getSmallestWidthDp(activity)
        statusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME)
        actionBarHeight = getActionBarHeight(activity)
        navigationBarHeight = getNavigationBarHeight(activity)
        navigationBarWidth = getNavigationBarWidth(activity)
        mHasNavigationBar = navigationBarHeight > 0
        mTranslucentStatusBar = translucentStatusBar
        mTranslucentNavBar = traslucentNavBar
    }

    @TargetApi(14)
    private fun getActionBarHeight(context: Context): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val tv = TypedValue()
            context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
            result =
                TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return result
    }

    @TargetApi(14)
    private fun getNavigationBarHeight(context: Context): Int {
        val res = context.resources
        val result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context)) {
                val key: String = if (mInPortrait) {
                    NAV_BAR_HEIGHT_RES_NAME
                } else {
                    NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME
                }
                return getInternalDimensionSize(res, key)
            }
        }
        return result
    }

    @TargetApi(14)
    private fun getNavigationBarWidth(context: Context): Int {
        val res = context.resources
        val result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context)) {
                return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME)
            }
        }
        return result
    }

    @TargetApi(14)
    private fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android")
        return if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // check override flag (see static block)
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            hasNav
        } else { // fallback
            !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    @SuppressLint("NewApi")
    private fun getSmallestWidthDp(activity: Activity): Float {
        val metrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        } else {
            activity.windowManager.defaultDisplay.getMetrics(metrics)
        }
        val widthDp = metrics.widthPixels / metrics.density
        val heightDp = metrics.heightPixels / metrics.density
        return widthDp.coerceAtMost(heightDp)
    }

    /**
     * Should a navigation bar appear at the bottom of the screen in the current
     * device configuration? A navigation bar may appear on the right side of
     * the screen in certain configurations.
     *
     * @return True if navigation should appear at the bottom of the screen, False otherwise.
     */
    val isNavigationAtBottom: Boolean
        get() = mSmallestWidthDp >= 600 || mInPortrait

    /**
     * Does this device have a system navigation bar?
     *
     * @return True if this device uses soft key navigation, False otherwise.
     */
    fun hasNavigtionBar(): Boolean {
        return mHasNavigationBar
    }

    /**
     * Get the layout inset for any system UI that appears at the top of the screen.
     *
     * @param withActionBar True to include the height of the action bar, False otherwise.
     * @return The layout inset (in pixels).
     */
    fun getPixelInsetTop(withActionBar: Boolean): Int {
        return (if (mTranslucentStatusBar) statusBarHeight else 0) + if (withActionBar) actionBarHeight else 0
    }

    /**
     * Get the layout inset for any system UI that appears at the bottom of the screen.
     *
     * @return The layout inset (in pixels).
     */
    val pixelInsetBottom: Int
        get() = if (mTranslucentNavBar && isNavigationAtBottom) {
            navigationBarHeight
        } else {
            0
        }

    /**
     * Get the layout inset for any system UI that appears at the right of the screen.
     *
     * @return The layout inset (in pixels).
     */
    val pixelInsetRight: Int
        get() = if (mTranslucentNavBar && !isNavigationAtBottom) {
            navigationBarWidth
        } else {
            0
        }
}