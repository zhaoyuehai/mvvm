package com.yuehai.mvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuehai.basic.BaseActivity
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.ActivitySnowBinding
import com.yuehai.mvvm.ui.adapter.DemoViewHolder
import com.yuehai.mvvm.ui.adapter.FloatAdapter
import com.yuehai.mvvm.widget.SNBBottomSheetBehavior

class SnowListDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySnowBinding.inflate(layoutInflater).apply {
            setContentView(root)
            toolbar.navButtonView.setOnClickListener {
                finish()
            }
            rvNested.layoutManager = LinearLayoutManager(this@SnowListDemoActivity)
            rvNested.adapter = object : RecyclerView.Adapter<DemoViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DemoViewHolder(
                    LayoutInflater.from(this@SnowListDemoActivity)
                        .inflate(R.layout.behavior_item_f, parent, false)
                )

                override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
                }

                override fun getItemCount() = 20
            }

            floatContent.layoutManager = LinearLayoutManager(this@SnowListDemoActivity)
            floatContent.adapter = FloatAdapter().apply {
                setItems(listOf("1111", "2222", "3333", "4444", "5555"))
            }
            SNBBottomSheetBehavior.from(behaviorContain as View).apply {
                peekHeight = 100
                state = SNBBottomSheetBehavior.STATE_COLLAPSED
                rvNested.post {
                    rvNested.bindSNBBottomSheetBehavior(this)
                }
            }
        }
    }
}