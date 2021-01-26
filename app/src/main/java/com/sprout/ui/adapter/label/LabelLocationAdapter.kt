package com.sprout.ui.adapter.label

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.data.model.bean.LocationInfo
import com.sprout.databinding.AdapterLabelLocationBinding

class LabelLocationAdapter:BaseQuickAdapter<LocationInfo,BaseDataBindingHolder<AdapterLabelLocationBinding>>(
    R.layout.adapter_label_location) {
    override fun convert(
        holder: BaseDataBindingHolder<AdapterLabelLocationBinding>,
        item: LocationInfo
    ) {
        holder.dataBinding!!.location = item
    }
}