package com.sprout.viewmodel.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.sprout.app.ext.download.openLocation
import com.sprout.app.ext.download.setAMapSearchApi
import com.sprout.app.network.apiService
import com.sprout.data.model.bean.LocationInfo
import com.sprout.data.model.bean.ThemeBen
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.ext.util.TAG
import me.hgj.jetpackmvvm.state.ResultState
import java.lang.ref.WeakReference

class ReleaseAddViewModel : BaseViewModel() {

    val themeBean: MutableLiveData<ResultState<ArrayList<ThemeBen>>> = MutableLiveData()
    val amapLocation: MutableLiveData<AMapLocation> = MutableLiveData()
    val locationInfoList: MutableLiveData<ArrayList<LocationInfo>> = MutableLiveData()

    fun getTheme() {
        request({ apiService.getTheme() }, themeBean)
    }

    //开启定位

    fun openLabelLocation(locationClient: AMapLocationClient) {
        openLocation(locationClient, ReleaseAMapLocationListener(amapLocation))
    }


    /**
     * 高德地图检索周边地址
     */
    fun setSearchApi(lat: Double, lon: Double) {

        setAMapSearchApi(lat, lon, ReleasePoiSearchListener())
    }


    inner class ReleasePoiSearchListener : PoiSearch.OnPoiSearchListener {
        override fun onPoiSearched(result: PoiResult?, p1: Int) {

            val query = result!!.query

            val pois = result.pois

            val list = arrayListOf<LocationInfo>()

            for (poi in pois) {

                val point = poi.latLonPoint //经纬度

                Log.d(TAG, "onPoiSearched: ${poi.businessArea}")
                val info =
                    LocationInfo(poi.title, poi.snippet, point.latitude, point.longitude)

                list.add(info)
            }

            locationInfoList.value = list

        }

        override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

        }

    }


     class  ReleaseAMapLocationListener(data: MutableLiveData<AMapLocation>) :
        AMapLocationListener {
        private val sData: WeakReference<MutableLiveData<AMapLocation>> = WeakReference(data)

        override fun onLocationChanged(p0: AMapLocation?) {
            val data = sData.get() ?: return
            data.value = p0
//            amapLocation.value = p0
        }

    }

}


