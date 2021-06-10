package com.yuehai.mvvm.ui

import android.os.Bundle
import android.view.View
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.yuehai.basic.BaseVMActivity
import com.yuehai.mvvm.BR
import com.yuehai.mvvm.R
import com.yuehai.mvvm.databinding.ActivityMapBinding
import com.yuehai.mvvm.vm.MapViewModel

/**
 * Created by zhaoyuehai 2021/5/8
 */
class MapActivity : BaseVMActivity<ActivityMapBinding, MapViewModel>(
    R.layout.activity_map,
    BR.mapVM,
    MapViewModel::class
) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //TODO
        binding.mapView.onCreate(savedInstanceState)
        binding.onMenuClick = View.OnClickListener {

        }
        binding.mapView.map.addMarker(
            MarkerOptions().position(LatLng(39.906901, 116.397972)).title("北京")
                .snippet("DefaultMarker")
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}