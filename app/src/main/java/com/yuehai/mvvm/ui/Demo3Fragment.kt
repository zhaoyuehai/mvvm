package com.yuehai.mvvm.ui

import android.app.Activity
import android.graphics.Color
import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo3Binding
import com.yuehai.mvvm.vm.Demo3ViewModel

class Demo3Fragment : BaseVMFragment<FragmentDemo3Binding, Demo3ViewModel>(
    R.layout.fragment_demo3,
    Demo3ViewModel::class.java,
    BR.demo3VM
) {
    override fun init(activity: Activity) {
        super.init(activity)
        viewDataBinding?.demo3Tv?.setTextColor(Color.MAGENTA)
    }
}
