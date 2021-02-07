package com.sprout.data.model.bean

data class SameCityBean(
    val data: List<DataX>,
)

data class DataX(
    val address: String,
    val avater: Any,
    val channelid: Int,
    val date: String,
    val goods: Int,
    val id: Int,
    val lat: Int,
    val lng: Int,
    val mood: String,
    val nickname: Any,
    val res: List<Re>,
    val themeid: Int,
    val title: String,
    val type: Int,
    val uid: String,
    val url: String
)

data class Re(
    val tags: List<TagX>,
    val url: String
)

data class TagX(
    val lat: Int,
    val lng: Int,
    val resid: Int,
    val tag_x: Int,
    val tag_y: Int,
    val tagid: Int,
    val tagname: String,
    val tagtype: Int
)