package com.sprout.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.data.model.bean.DataX
import com.sprout.databinding.AdapterSameCityBinding

class SameCityAdapter():BaseQuickAdapter<DataX,BaseDataBindingHolder<AdapterSameCityBinding>>(
    R.layout.adapter_same_city) {
    override fun convert(
        holder: BaseDataBindingHolder<AdapterSameCityBinding>,
        item: DataX
    ) {


    }


}