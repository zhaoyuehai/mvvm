package com.yuehai.mvvm.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yuehai.basic.BaseActivity
import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.ActivityFragmentsBinding
import com.yuehai.mvvm.vm.Demo4ViewModel

/**
 * Created by zhaoyuehai 2021/5/8
 */
class FragmentContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO
        val binding =
            DataBindingUtil.setContentView<ActivityFragmentsBinding>(
                this,
                R.layout.activity_fragments
            )
        binding.onBackClick = View.OnClickListener {
            finish()
        }
        for (i in 0 until 4) {
            binding.commonContainerTbl.addTab(binding.commonContainerTbl.newTab())
        }
        binding.commonContainerVp2.let {
            it.adapter =
                object : FragmentStateAdapter(this) {
                    override fun getItemCount() = 4
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
                        2 -> Demo3Fragment()
                        else -> BaseVMFragment(
                            R.layout.fragment_demo4,
                            BR.demo4VM,
                            Demo4ViewModel::class
                        )
                    }
                }
            TabLayoutMediator(binding.commonContainerTbl, it) { tab, position ->
                tab.text = "Tab$position"
            }.attach()
        }
    }
}