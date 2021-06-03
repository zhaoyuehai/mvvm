package com.yuehai.mvvm.ui

import com.yuehai.basic.BaseVMActivity
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.databinding.ActivitySecondBinding
import com.yuehai.mvvm.vm.SecondViewModel

/**
 * Created by zhaoyuehai 2021/5/8
 */
class SecondActivity : BaseVMActivity<ActivitySecondBinding, SecondViewModel>(
    ActivitySecondBinding::inflate,
    BR.secondVM,
    SecondViewModel::class.java
)