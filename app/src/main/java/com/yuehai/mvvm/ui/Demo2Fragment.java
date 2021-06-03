package com.yuehai.mvvm.ui;

import com.yuehai.basic.BaseVMFragment;
import com.yuehai.mvvm.BR;
import com.yuehai.mvvm.R;
import com.yuehai.mvvm.databinding.FragmentDemo2Binding;
import com.yuehai.mvvm.vm.Demo2ViewModel;

public class Demo2Fragment extends BaseVMFragment<FragmentDemo2Binding, Demo2ViewModel> {
    public Demo2Fragment() {
        super(R.layout.fragment_demo2, FragmentDemo2Binding::bind, BR.demo2VM, Demo2ViewModel.class);
    }
}