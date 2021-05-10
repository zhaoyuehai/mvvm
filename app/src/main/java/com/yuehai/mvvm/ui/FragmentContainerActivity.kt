package com.yuehai.mvvm.ui

import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
        val tabLayout = findViewById<TabLayout>(R.id.common_container_tbl)
        for (i in 0 until 3) {
            tabLayout.addTab(tabLayout.newTab())
        }
        findViewById<ViewPager2>(R.id.common_container_vp2).let {
            it.adapter =
                object : FragmentStateAdapter(this) {
                    override fun getItemCount() = 3
                    override fun createFragment(position: Int) = when (position) {
                        0 -> Demo1Fragment()
                        1 -> Demo2Fragment().apply {
                            arguments = Bundle().apply {
                                putString(
                                    "title",
                                    "Demo2 arguments携带的Title \n 这是个Demo2Fragment.java类"
                                )
                            }
                        }
                        else -> Demo3Fragment()
                    }
                }
            TabLayoutMediator(tabLayout, it) { tab, position ->
                tab.text = "Tab$position"
            }.attach()
        }
    }
}