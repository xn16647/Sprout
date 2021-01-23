package com.sprout.ui.adapter

import android.util.Log
import com.bumptech.glide.Glide
import com.sprout.R
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.util.TAG

class BannerLabelAdapter():BaseBannerAdapter<String>() {
    override fun bindData(
        holder: BaseViewHolder<String>?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {

        Log.e(TAG, "bindData: $data" )

        Glide.with(appContext).load(data).into(holder!!.findViewById(R.id.banner_image))
    }

    override fun getLayoutId(viewType: Int): Int {

        return R.layout.adapter_banner_label
    }


}