package com.yuehai.mvvm.ui

import com.yuehai.basic.BaseVMActivity
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.ActivitySecondBinding
import com.yuehai.mvvm.vm.SecondViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by zhaoyuehai 2021/5/8
 */
@AndroidEntryPoint
class SecondActivity : BaseVMActivity<ActivitySecondBinding, SecondViewModel>(
    R.layout.activity_second,
    BR.secondVM,
    SecondViewModel::class
)