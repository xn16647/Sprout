package com.sprout.ui.adapter

import android.util.Log
import com.bumptech.glide.Glide
import com.sprout.R
import com.sprout.app.weight.photodraweeview.PictureTagFrameLayout
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.util.TAG

class BannerLabelAdapter():BaseBannerAdapter<String>() {

    private lateinit var pictureTagFrameLayout:PictureTagFrameLayout
    override fun bindData(
        holder: BaseViewHolder<String>?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {

        Log.e(TAG, "bindData: $data" )

        Glide.with(appContext).load(data).into(holder!!.findViewById(R.id.banner_image))
        pictureTagFrameLayout = holder.itemView as PictureTagFrameLayout

    }

    override fun getLayoutId(viewType: Int): Int {

        return R.layout.adapter_banner_label
    }

    fun getPrimaryItem():PictureTagFrameLayout{
        return pictureTagFrameLayout
    }

}