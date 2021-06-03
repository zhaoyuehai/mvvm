package com.yuehai.mvvm.ui;

import android.graphics.Color;
import android.os.Bundle;

import com.yuehai.basic.BaseVMFragment;
import com.yuehai.mvvm.BR;
import com.yuehai.mvvm.R;
import com.yuehai.mvvm.databinding.FragmentDemo2Binding;
import com.yuehai.mvvm.vm.Demo2ViewModel;

import org.jetbrains.annotations.Nullable;

public class Demo2Fragment extends BaseVMFragment<FragmentDemo2Binding, Demo2ViewModel> {
    public Demo2Fragment() {
        super(R.layout.fragment_demo2, FragmentDemo2Binding::bind, BR.demo2VM, Demo2ViewModel.class);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        getBinding().demo2Tv.setTextColor(Color.BLUE);
    }
}