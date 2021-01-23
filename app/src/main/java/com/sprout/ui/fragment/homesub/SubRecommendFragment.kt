package com.sprout.ui.fragment.homesub

import android.os.Bundle
import com.sprout.viewmodel.subhome.SubRecommendViewModel
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentSubRecommendBinding

class SubRecommendFragment: BaseFragment<SubRecommendViewModel, FragmentSubRecommendBinding>() {
    override fun layoutId(): Int {
       return R.layout.fragment_sub_recommend
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}