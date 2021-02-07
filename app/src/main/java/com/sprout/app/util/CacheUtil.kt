package com.sprout.app.util

import android.text.TextUtils
import com.google.gson.Gson
import com.sprout.data.model.bean.Poi
import com.sprout.data.model.bean.UserInfo
import com.tencent.mmkv.MMKV

object CacheUtil {

    val TAG_CONNECTOR = "f1hffc2x1vz5ff41ea4t89ae4ta625ffas6161"

    /**
     * 获取保存的账户信息
     */
    fun getUser(): UserInfo? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfo::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: UserInfo?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
            setIsLogin(false)
        } else {
            kv.encode("user", Gson().toJson(userResponse))
            setIsLogin(true)
        }

    }

    /**
     * 存储最近标记
     */

    fun setTag(tag: String,type:String) {
        val kv = MMKV.mmkvWithID("app")

        val realTag = "${tag}${TAG_CONNECTOR}${type}"
        val getTag = getTag()
        if (getTag==null){
            kv.encode("tag",realTag)
        }else{

            if(getTag.contains(tag)){
                return
            }

            if (getTag.contains(",")){

               val tags =  getTag.split(",")
                if (tags.size<5){
                    kv.encode("tag", "$realTag,$getTag")
                }else{
                   val index = getTag.lastIndexOf(',')
                    val tag2 = getTag.substring(0,index)
                    kv.encode("tag","$realTag,$tag2")
                }

            }else{
                kv.encode("tag", "$realTag,$getTag")
            }

        }


    }


    /**
     * 获取最近标记
     */

    fun getTag():String?{
        val kv = MMKV.mmkvWithID("app")
        return kv.getString("tag", null)
    }

    fun setToken(token: String?) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("token", token)
    }

    fun getToken(): String? {

        val kv = MMKV.mmkvWithID("app")
        return kv.getString("token", "")

    }

    //保存位置信息
    fun setPoi(poi: Poi) {
        val kv = MMKV.mmkvWithID("app")

        kv.encode("poi", Gson().toJson(poi))
    }


    /**
     * 获取保存的位置信息
     */
    fun getPoi(): Poi {
        val kv = MMKV.mmkvWithID("app")
        val poiStr = kv.decodeString("poi")

        return Gson().fromJson(poiStr, Poi::class.java)
    }


    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("first", true)
    }

    /**
     * 是否是第一次登陆
     */
    fun setFirst(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("first", first)
    }


    fun setSearchHistoryData(searchResponseStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("history", searchResponseStr)
    }
}