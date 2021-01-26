package com.sprout.app.network

import com.sprout.data.model.bean.*
import retrofit2.http.*

/**
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

    //标签获取品牌列表
    @GET("tag/brand")
    suspend fun getLabelTagBrand(@Query("page") page: Int):ApiResponse<ApiPagerResponse<ArrayList<LabelTag>>>

    //标签获取商品列表
    @GET("tag/goods")
    suspend fun getLabelTagGoods(@Query("page") page: Int):ApiResponse<ApiPagerResponse<ArrayList<LabelGoodsData>>>

}