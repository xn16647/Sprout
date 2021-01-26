package com.sprout.viewmodel

import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.poisearch.PoiSearch
import com.sprout.app.ext.download.openLocation
import com.sprout.app.network.apiService
import com.sprout.app.util.UploadHelper
import com.sprout.data.model.bean.ApiPagerResponse
import com.sprout.data.model.bean.Data
import com.sprout.data.model.bean.LabelGoodsData
import com.sprout.data.model.bean.LabelTag
import com.sprout.data.repository.request.HttpRequestCoroutine
import com.sprout.data.repository.request.HttpRequestManger
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class LabelViewModel : BaseViewModel() {


    val labelGoodsData: MutableLiveData<ResultState<ApiPagerResponse<ArrayList<LabelGoodsData>>>> = MutableLiveData()
    val labelBrandData: MutableLiveData<ResultState<ApiPagerResponse<ArrayList<LabelTag>>>> = MutableLiveData()

    val aliYunList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val amapLocation:MutableLiveData<AMapLocation> = MutableLiveData()
    val onPoiSearchListener:MutableLiveData<PoiSearch.OnPoiSearchListener>  = MutableLiveData()

    fun getLabelGoodsList(page: Int) {
        request({ HttpRequestCoroutine.getLabelGoodsList(page) }, labelGoodsData)
    }


    fun getLabelBrandList(page: Int) {
        request({ HttpRequestCoroutine.getLabelBrandList(page) }, labelBrandData)
    }


    //上传图片至阿里云
    fun upImgToAliyun(list: ArrayList<String>) {


        val imageList:ArrayList<String> = arrayListOf()


        for (s in list) {
            val uploadImage = UploadHelper.uploadImage(s)
            imageList.add(uploadImage)

        }

        aliYunList.value = imageList

    }

    //开启定位

    fun openLabelLocation(){
        openLocation {
            amapLocation.value = it
        }
    }



}