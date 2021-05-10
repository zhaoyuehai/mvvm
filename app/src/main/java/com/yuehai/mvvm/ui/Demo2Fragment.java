package com.yuehai.mvvm.ui;

import android.app.Activity;
import android.graphics.Color;

import com.yuehai.basic.BaseVMFragment;
import com.yuehai.mvvm.BR;
import com.yuehai.mvvm.R;
import com.yuehai.mvvm.databinding.FragmentDemo2Binding;
import com.yuehai.mvvm.vm.Demo2ViewModel;

import org.jetbrains.annotations.NotNull;

public class Demo2Fragment extends BaseVMFragment<FragmentDemo2Binding, Demo2ViewModel> {
    public Demo2Fragment() {
        super(R.layout.fragment_demo2, Demo2ViewModel.class, BR.demo2VM);
    }

    @Override
    public void addObserver(@NotNull Activity activity) {
        super.addObserver(activity);
        FragmentDemo2Binding viewDataBinding = getViewDataBinding();
        if (viewDataBinding != null) {
            viewDataBinding.demo2Tv.setTextColor(Color.BLUE);
        }
    }
}