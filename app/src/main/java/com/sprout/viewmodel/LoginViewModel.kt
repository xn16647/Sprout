package com.sprout.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.sprout.R
import com.sprout.data.model.bean.ApiResponse
import com.sprout.data.model.bean.RegisterMessage
import com.sprout.data.repository.request.HttpRequestManger
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class LoginViewModel : BaseViewModel() {


    val url = MutableLiveData<String>().apply {
        value = RawResourceDataSource.buildRawResourceUri(R.raw.login_bg).toString()
    }

    val registerInfo = MutableLiveData<ResultState<RegisterMessage>>()


    fun login(userName: String, userPsw: String) {
        request({ HttpRequestManger().login(userName, userPsw) },registerInfo )
    }

    fun register(userName: String, userPsw: String, imei: String, lng: String, lat: String) {
        request({ HttpRequestManger().register(userName, userPsw, imei, lng, lat) }, registerInfo)
    }

}