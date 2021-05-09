package com.yuehai.mvvm.ui

import android.os.Bundle
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
class MapActivity : BaseVMActivity<ActivityMapBinding, MapViewModel>() {
    override val layout = R.layout.activity_map
    override val variableId = BR.mapVM
    override val viewModelClass = MapViewModel::class.java
    override fun getCustomToolbar() = viewDataBinding?.toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding?.mapView?.let { mapView ->
            mapView.onCreate(savedInstanceState)
            mapView.map?.addMarker(
                MarkerOptions().position(LatLng(39.906901, 116.397972)).title("北京")
                    .snippet("DefaultMarker")
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        viewDataBinding?.mapView?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        viewDataBinding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        viewDataBinding?.mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        viewDataBinding?.mapView?.onSaveInstanceState(outState)
    }
}