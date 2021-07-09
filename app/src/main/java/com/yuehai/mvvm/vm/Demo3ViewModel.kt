package com.yuehai.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuehai.basic.BaseViewModel
import com.yuehai.mvvm.data.DataRepository
import com.yuehai.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created by zhaoyuehai 2021/4/28
 */
@HiltViewModel
class Demo3ViewModel @Inject constructor(private val repository: DataRepository) :
    BaseViewModel() {
    val title = MutableLiveData("这个Demo3Fragment.kt类")
    val downLoadMessage = MutableLiveData("")
    val uploadMessage = MutableLiveData("")
    val pickFile = SingleLiveEvent<Boolean>()
    fun download() {
        showLoading("正在下载...")
        repository.download(
            viewModelScope,
            "https://power.cnecloud.com/upgrade/app-power-release.apk",
            "app-power-release.apk", object : DataRepository.ProgressListener {
                override fun onProgress(percent: Long, total: Long) {
                    downLoadMessage.value = "下载中：${percent}% (${total}B)"
                    showLoading("${percent}%...")
                }

                override fun onResult(total: Long?, message: String) {
                    showToast(message)
                    dismissLoading()
                    if (total != null) {
                        downLoadMessage.value = "下载完成(${total}B)"
                    }
                }
            })
    }

    fun upload() {
        pickFile.value = true
    }

    fun upload(filePath: String) {
        showLoading("正在上传...")
        repository.upload(viewModelScope, listOf(File(filePath)), object :
            DataRepository.ProgressListener {
            override fun onProgress(percent: Long, total: Long) {
                uploadMessage.value = "上传进度：${percent}% / ${total}B"
                showLoading("${percent}%...")
            }

            override fun onResult(total: Long?, message: String) {
                showToast(message)
                dismissLoading()
                if (total != null) {
                    uploadMessage.value = "上传完成 ${total}B"
                }
            }
        })
    }
}