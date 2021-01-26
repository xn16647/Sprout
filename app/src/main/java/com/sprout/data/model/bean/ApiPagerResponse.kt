package com.sprout.data.model.bean

import java.io.Serializable

/**
 *  分页数据的基类
 */
data class ApiPagerResponse<T>(
    var data: T,
    var count: Int,
    var totalPages: Int,
    var currentPage: Int,
) : Serializable {
    /**
     * 数据是否为空
     */
    fun isEmpty() = (data as List<*>).size == 0


    /**
     * 是否还有更多数据
     */
    fun hasMore() = totalPages>currentPage
}