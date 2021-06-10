package com.yuehai.mvvm.ui

import android.content.DialogInterface
import android.os.Bundle
import com.yuehai.basic.BaseVMFragment
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo1Binding
import com.yuehai.mvvm.vm.Demo1ViewModel
import com.yuehai.util.DialogUtil

/**
 * Created by zhaoyuehai 2021/4/29
 */
class Demo1Fragment : BaseVMFragment<FragmentDemo1Binding, Demo1ViewModel>(
    R.layout.fragment_demo1,
    BR.demo1VM,
    Demo1ViewModel::class
){
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.bottomDialog.observe(this, {
            if (it != null) {
                activity?.let { activity ->
                    DialogUtil.showBottomDialog(
                        activity, it.first
                    ) { _, which ->
                        it.second.invoke(which == DialogInterface.BUTTON_POSITIVE)
                    }
                }
            }
        })
    }
}
