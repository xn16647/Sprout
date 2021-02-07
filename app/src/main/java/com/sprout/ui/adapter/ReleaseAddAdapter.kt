package com.sprout.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.data.model.bean.LocationInfo
import com.sprout.databinding.AdapterReleaseAddLocationBinding

class ReleaseAddAdapter:BaseQuickAdapter<LocationInfo,BaseDataBindingHolder<AdapterReleaseAddLocationBinding>>(
    R.layout.adapter_release_add_location) {
    override fun convert(
        holder: BaseDataBindingHolder<AdapterReleaseAddLocationBinding>,
        item: LocationInfo
    ) {
        holder.dataBinding!!.location = item
    }
}