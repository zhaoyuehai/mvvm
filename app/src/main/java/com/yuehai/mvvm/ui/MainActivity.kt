package com.yuehai.mvvm.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yuehai.basic.BaseVMActivity
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.ActivityMainBinding
import com.yuehai.mvvm.util.observe
import com.yuehai.mvvm.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main,
    BR.mainVM,
    MainViewModel::class
), ActivityResultCallback<ActivityResult> {

    private lateinit var secondLauncher: ActivityResultLauncher<Intent>

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        secondLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), this)
        observe(viewModel.toPage, ::navigatePage)
    }

    private fun navigatePage(position: Int) {
        when (position) {
            0 -> secondLauncher.launch(Intent(this, SecondActivity::class.java))
            1 -> startActivity(Intent(this, FragmentContainerActivity::class.java))
            2 -> startActivity(Intent(this, MapActivity::class.java))
            3 -> startActivity(Intent(this, SnowListDemoActivity::class.java))
        }
    }

    override fun onActivityResult(result: ActivityResult?) {
    }

    override fun onDestroy() {
        secondLauncher.unregister()
        super.onDestroy()
    }

    private var exitTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 1500) {
            showToast("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}