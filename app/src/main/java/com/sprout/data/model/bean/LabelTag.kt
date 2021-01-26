package com.sprout.data.model.bean

data class LabelTag(
    var id: Int,
    var name: String,
    var sort: Int,
    var url: String = "",
    var title:String = ""
)