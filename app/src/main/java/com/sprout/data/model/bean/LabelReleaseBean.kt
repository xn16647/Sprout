package com.sprout.data.model.bean

data class LabelReleaseBean(
    val address: String,
    val channelid: Int,
    val lat: Int,
    val lng: Int,
    val mood: String,
    val res: List<Res>,
    val themeid: Int,
    val title: String,
    val type: Int
)

data class Res(
    val tags: List<Tag>,
    val url: String
)

data class Tag(
    val id: Int,
    val lat: Int,
    val lng: Int,
    val name: String,
    val type: Int,
    val x: Int,
    val y: Int
)