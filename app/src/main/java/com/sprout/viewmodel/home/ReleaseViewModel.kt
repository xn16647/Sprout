package com.sprout.viewmodel.home

import androidx.lifecycle.MutableLiveData
import com.sprout.app.network.apiService
import com.sprout.app.util.UploadHelper
import com.sprout.data.model.bean.ApiResponse
import com.sprout.data.model.bean.ReleaseBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.requestNoCheck
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.RequestBody

class ReleaseViewModel:BaseViewModel() {

    val aliYunList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val release:MutableLiveData<ResultState<ApiResponse<ReleaseBean>>> = MutableLiveData()

    //上传图片至阿里云
    fun upImgToAliyun(list: ArrayList<String>) {


        val imageList:ArrayList<String> = arrayListOf()


        for (s in list) {
            val uploadImage = UploadHelper.uploadImage(s,"image/jpeg")
            imageList.add(uploadImage)

        }

        aliYunList.value = imageList

    }

    //发布动态
    fun release(requestBody: RequestBody){
        requestNoCheck({ apiService.release(requestBody)},release)
    }

}