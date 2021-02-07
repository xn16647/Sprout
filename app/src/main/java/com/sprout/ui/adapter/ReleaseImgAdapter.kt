package com.sprout.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterReleaseImgBinding

class ReleaseImgAdapter:BaseQuickAdapter<String,BaseDataBindingHolder<AdapterReleaseImgBinding>>(R.layout.adapter_release_img) {


    override fun convert(holder: BaseDataBindingHolder<AdapterReleaseImgBinding>, item: String) {

        holder.setIsRecyclable(false)

        if (item.isNotEmpty()) {
            Glide.with(context).load(item).into(holder.dataBinding!!.imageView)
        }

    }
}