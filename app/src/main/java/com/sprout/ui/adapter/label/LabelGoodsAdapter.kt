package com.sprout.ui.adapter.label

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.data.model.bean.LabelGoodsData
import com.sprout.databinding.AdapterLabelTagGoodsBinding

class LabelGoodsAdapter: BaseQuickAdapter<LabelGoodsData, BaseDataBindingHolder<AdapterLabelTagGoodsBinding>>(R.layout.adapter_label_tag_goods) {

    override fun convert(holder: BaseDataBindingHolder<AdapterLabelTagGoodsBinding>, item: LabelGoodsData) {
        holder.dataBinding!!.label = item
    }

}