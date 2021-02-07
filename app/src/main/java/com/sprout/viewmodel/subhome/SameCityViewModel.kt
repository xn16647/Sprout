package com.sprout.viewmodel.subhome

import androidx.lifecycle.MutableLiveData
import com.sprout.data.model.bean.SameCityBean
import com.sprout.data.repository.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class SameCityViewModel:BaseViewModel() {


    val sameCityBean:MutableLiveData<ResultState<SameCityBean>> = MutableLiveData()


    fun getSameCityBean(command:Int,page:Int){

       request({ HttpRequestCoroutine.getSameCityData(command)},sameCityBean)

    }

}