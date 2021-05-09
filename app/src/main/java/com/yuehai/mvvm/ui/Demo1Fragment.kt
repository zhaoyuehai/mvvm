package com.yuehai.mvvm.ui

import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo1Binding
import com.yuehai.mvvm.vm.Demo1ViewModel

/**
 * Created by zhaoyuehai 2021/4/29
 */
class Demo1Fragment :
    BaseVMFragment<FragmentDemo1Binding, Demo1ViewModel>(
        R.layout.fragment_demo1,
        Demo1ViewModel::class.java
    ) {
    override val variableId = BR.demo1VM

    override fun addObserver() {
        super.addObserver()
        viewModel.toPage.observe(this) {
            if (it) viewDataBinding?.demoTest?.text = "你点击我了哈哈😁"
        }
    }
}