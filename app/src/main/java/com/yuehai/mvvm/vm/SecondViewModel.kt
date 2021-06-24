package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuehai.basic.BaseViewModel
import com.yuehai.mvvm.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class SecondViewModel @Inject constructor(private val repository: DataRepository) :
    BaseViewModel() {
    val title = "普通MVVM页面"
    val testData = MutableLiveData<String>()
    override fun initData() {
        super.initData()
        showLoading()
        repository.loadListData(viewModelScope) {
            dismissLoading()
            if (it.isOK()) {
                testData.value = it.data().toString()
            } else {
                showToast(it.message())
            }
        }
    }
}