package com.sprout.app.network

import com.sprout.data.model.bean.ApiResponse
import com.sprout.data.model.bean.RegisterMessage
import com.sprout.data.model.bean.UserInfo
import retrofit2.http.*

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL = "http://sprout.cdwan.cn/api/"
    }


    //注册
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("imei") imei: String,
        @Field("lng") lng: String,
        @Field("lat") lat: String
    ): ApiResponse<RegisterMessage>


    //登录
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String):ApiResponse<RegisterMessage>


    //获取动态详情
    @GET("trends/trendsDetail")
    suspend fun getTrendsDetail(@Query("trendsid") id: Int)

}