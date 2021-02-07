package com.sprout.viewmodel

import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.sprout.app.ext.download.openLocation
import com.sprout.app.ext.download.setAMapSearchApi
import com.sprout.app.util.UploadHelper
import com.sprout.data.model.bean.ApiPagerResponse
import com.sprout.data.model.bean.LabelGoodsData
import com.sprout.data.model.bean.LabelTag
import com.sprout.data.model.bean.LocationInfo
import com.sprout.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import java.lang.ref.WeakReference

class LabelViewModel : BaseViewModel() {


    val labelGoodsData: MutableLiveData<ResultState<ApiPagerResponse<ArrayList<LabelGoodsData>>>> =
        MutableLiveData()
    val labelBrandData: MutableLiveData<ResultState<ApiPagerResponse<ArrayList<LabelTag>>>> =
        MutableLiveData()
    val aliYunList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val amapLocation: MutableLiveData<AMapLocation> = MutableLiveData()
    val locationInfoList: MutableLiveData<ArrayList<LocationInfo>> = MutableLiveData()

    fun getLabelGoodsList(page: Int) {
        request({ HttpRequestCoroutine.getLabelGoodsList(page) }, labelGoodsData)
    }


    fun getLabelBrandList(page: Int) {
        request({ HttpRequestCoroutine.getLabelBrandList(page) }, labelBrandData)
    }


    //上传图片至阿里云
    fun upImgToAliyun(list: ArrayList<String>, mediaType: String) {


        val imageList: ArrayList<String> = arrayListOf()


        for (s in list) {
            val uploadImage = UploadHelper.uploadImage(s, mediaType)
            imageList.add(uploadImage)

        }

        aliYunList.value = imageList

    }

    //开启定位

    //开启定位

    fun openLabelLocation(locationClient: AMapLocationClient) {
        openLocation(locationClient,LabelAMapLocationListener(amapLocation))
    }


    /**
     * 高德地图检索周边地址
     */
    fun setSearchApi(lat: Double, lon: Double) {

        setAMapSearchApi(lat, lon, object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(result: PoiResult?, p1: Int) {

                val query = result!!.query

                val pois = result.pois

                val list = arrayListOf<LocationInfo>()

                for (poi in pois) {

                    val point = poi.latLonPoint //经纬度

                    val info = LocationInfo(
                        poi.title,
                        poi.adName + poi.businessArea + poi.snippet,
                        point.latitude,
                        point.longitude
                    )

                    list.add(info)
                }

                locationInfoList.value = list

            }

            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }

        })
    }

    class LabelAMapLocationListener(aMapLocation: MutableLiveData<AMapLocation>) :
        AMapLocationListener {

        private val wAMapLocation: WeakReference<MutableLiveData<AMapLocation>> =
            WeakReference(aMapLocation)

        override fun onLocationChanged(data: AMapLocation?) {
            val amap = wAMapLocation.get() ?: return
            amap.value = data
        }

    }

}