package com.sprout.app.ext.download

import androidx.fragment.app.Fragment
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

fun BaseViewModel.openLocation(l: AMapLocationListener){
    //初始化定位
    val mLocationClient =
        AMapLocationClient(appContext)
    //设置定位回调监听
    mLocationClient.setLocationListener(l)
    //初始化定位参数
    val mLocationOption = AMapLocationClientOption()
    //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
    //设置是否只定位一次,默认为false
    mLocationOption.setOnceLocation(true)
    //给定位客户端对象设置定位参数
    mLocationClient.setLocationOption(mLocationOption)
    //启动定位
    mLocationClient.startLocation()
}