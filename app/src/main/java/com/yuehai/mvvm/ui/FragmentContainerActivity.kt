package com.yuehai.mvvm.ui

import android.os.Bundle
import com.yuehai.basic.BaseActivity
import com.yuehai.mvvm.R
import com.yuehai.util.StatusBarUtil
import com.yuehai.widget.CustomToolbar

/**
 * Created by zhaoyuehai 2021/5/8
 */
class FragmentContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        findViewById<CustomToolbar>(R.id.toolbar).apply {
            setTitle("Fragment容器页")
            navButtonView.setOnClickListener {
                finish()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.common_container_fl, Demo1Fragment())
        transaction.commitAllowingStateLoss()
    }
}