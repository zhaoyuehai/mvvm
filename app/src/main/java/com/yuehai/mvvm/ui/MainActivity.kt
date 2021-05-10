package com.yuehai.mvvm.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.yuehai.basic.BaseVMActivity
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.ActivityMainBinding
import com.yuehai.mvvm.vm.MainViewModel

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {
    override val layout = R.layout.activity_main
    override val variableId = BR.mainVM
    override val viewModelClass = MainViewModel::class.java

    private val secondLauncher =
        registerForActivityResult(object : ActivityResultContract<String?, Boolean>() {
            override fun createIntent(context: Context, input: String?) =
                Intent(context, SecondActivity::class.java)

            override fun parseResult(resultCode: Int, intent: Intent?) = resultCode == RESULT_OK
        }) {
        }

    override fun addObserver() {
        super.addObserver()
        viewModel.toPage.observe(this, {
            when (it) {
                0 -> secondLauncher.launch(null)
                1 -> startActivity(Intent(this, FragmentContainerActivity::class.java))
                2 -> startActivity(Intent(this, MapActivity::class.java))
                else -> {
                }
            }
        })
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