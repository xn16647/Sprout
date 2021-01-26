package com.sprout.ui.adapter.label

import android.view.View
import android.widget.Adapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.data.model.bean.LabelTag
import com.sprout.databinding.AdapterLabelBrandBinding
import com.sprout.databinding.AdapterLabelTagBinding

class LabelBrandAdapter :
    BaseQuickAdapter<LabelTag, BaseDataBindingHolder<AdapterLabelBrandBinding>>(R.layout.adapter_label_brand) {

    override fun convert(holder: BaseDataBindingHolder<AdapterLabelBrandBinding>, item: LabelTag) {
        holder.dataBinding!!.labelGoods = item
    }
}