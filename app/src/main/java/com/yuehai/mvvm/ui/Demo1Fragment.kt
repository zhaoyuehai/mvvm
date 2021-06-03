package com.yuehai.mvvm.ui

import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo1Binding
import com.yuehai.mvvm.vm.Demo1ViewModel

/**
 * Created by zhaoyuehai 2021/4/29
 */
class Demo1Fragment : BaseVMFragment<FragmentDemo1Binding, Demo1ViewModel>(
    R.layout.fragment_demo1,
    FragmentDemo1Binding::bind,
    BR.demo1VM,
    Demo1ViewModel::class.java
)
