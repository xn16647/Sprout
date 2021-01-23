package com.sprout.ui.fragment.homesub

import android.os.Bundle
import com.sprout.R
import com.sprout.viewmodel.subhome.SameCityViewModel
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentSameCityBinding

class SameCityFragment: BaseFragment<SameCityViewModel, FragmentSameCityBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_same_city
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}