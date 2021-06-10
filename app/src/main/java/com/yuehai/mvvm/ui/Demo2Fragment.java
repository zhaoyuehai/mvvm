package com.yuehai.mvvm.ui;

import com.yuehai.basic.BaseVMFragment;
import com.yuehai.mvvm.BR;
import com.yuehai.mvvm.R;
import com.yuehai.mvvm.databinding.FragmentDemo2Binding;
import com.yuehai.mvvm.vm.Demo2ViewModel;

import kotlin.jvm.JvmClassMappingKt;

public class Demo2Fragment extends BaseVMFragment<FragmentDemo2Binding, Demo2ViewModel> {
    public Demo2Fragment() {
        super(R.layout.fragment_demo2, BR.demo2VM, JvmClassMappingKt.getKotlinClass(Demo2ViewModel.class));
    }
}