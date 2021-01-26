package com.sprout.ui.adapter.label

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.app.util.CacheUtil
import com.sprout.databinding.AdapterRecentTagsBinding
import com.zhpan.bannerview.BaseViewHolder

class RecentTagsAdapter:BaseQuickAdapter<String,BaseDataBindingHolder<AdapterRecentTagsBinding>>(R.layout.adapter_recent_tags) {
    override fun convert(holder: BaseDataBindingHolder<AdapterRecentTagsBinding>, item: String) {

        val split = item.split(CacheUtil.TAG_CONNECTOR)

        holder.setText(R.id.text_pic_tag_left,split[0])

        val imageView = holder.getView<ImageView>(R.id.image_pic_tag_left)

        when (split[1].toInt()) {
            0 -> imageView.setImageResource(R.mipmap.tag_btn_brand_normal)
            1 -> imageView.setImageResource(R.mipmap.tag_btn_commodity_normal)
            2 -> imageView.setImageResource(R.mipmap.tag_btn_user_normal)
            3 -> imageView.setImageResource(R.mipmap.tag_btn_location_normal)
        }

    }
}