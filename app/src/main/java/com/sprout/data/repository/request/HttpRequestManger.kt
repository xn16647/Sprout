package com.sprout.data.repository.request

import com.sprout.app.network.ApiService
import com.sprout.app.network.NetworkApi
import com.sprout.app.network.apiService
import com.sprout.data.model.bean.ApiResponse
import com.sprout.data.model.bean.RegisterMessage

/**
 * 描述　: 处理协程的请求类
 */

val HttpRequestCoroutine: HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManger()
}

class HttpRequestManger {

    /**
     * 登录
     */
    suspend fun login(userName:String,userPsw:String):ApiResponse<RegisterMessage>{
       return apiService.login(userName,userPsw)
    }

    /**
     * 注册
     */
    suspend fun register(userName: String,userPsw: String,imei:String,lng:String,lat:String):ApiResponse<RegisterMessage>{
        return apiService.register(userName,userPsw,imei,lng,lat)
    }



}