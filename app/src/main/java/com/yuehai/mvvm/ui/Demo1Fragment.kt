package com.yuehai.mvvm.ui

import android.os.Bundle
import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo1Binding
import com.yuehai.mvvm.util.observe
import com.yuehai.mvvm.util.showBottomDialog
import com.yuehai.mvvm.vm.Demo1ViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by zhaoyuehai 2021/4/29
 */
@AndroidEntryPoint
class Demo1Fragment : BaseVMFragment<FragmentDemo1Binding, Demo1ViewModel>(
    R.layout.fragment_demo1,
    BR.demo1VM,
    Demo1ViewModel::class
) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        observe(viewModel.bottomDialog, ::showBottomDialog)
    }
}
