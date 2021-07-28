package com.yuehai.mvvm.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.yuehai.basic.BaseVMFragment
import com.yuehai.basic.getLoadingDialog
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.FragmentDemo3Binding
import com.yuehai.mvvm.util.FileUtil
import com.yuehai.mvvm.util.observe
import com.yuehai.mvvm.vm.Demo3ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Demo3Fragment : BaseVMFragment<FragmentDemo3Binding, Demo3ViewModel>(
    R.layout.fragment_demo3,
    BR.demo3VM,
    Demo3ViewModel::class
) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.demo3Tv.setTextColor(Color.MAGENTA)
        getLoadingDialog()?.setOnCancelListener {
            viewModel.onCancelLoading()
        }
        observe(viewModel.pickFile, ::pickFile)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data?.data?.let { uri ->
                    val filePath = FileUtil.getPath(requireContext(), uri)
                    if (filePath != null) {
                        viewModel.upload(filePath)
                    }
                }
            }
        }

    private fun pickFile(pickFile: Boolean) {
        if (pickFile) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                galleryLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "*/*"
                })
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    122
                )
            }
        }
    }
}
