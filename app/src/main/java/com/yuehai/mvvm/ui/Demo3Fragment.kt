package com.yuehai.mvvm.ui

import android.graphics.Color
import android.os.Bundle
import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo3Binding
import com.yuehai.mvvm.vm.Demo3ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Demo3Fragment : BaseVMFragment<FragmentDemo3Binding, Demo3ViewModel>(
    R.layout.fragment_demo3,
    BR.demo3VM,
    Demo3ViewModel::class
) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.demo3Tv.setTextColor(Color.MAGENTA)
    }
}
