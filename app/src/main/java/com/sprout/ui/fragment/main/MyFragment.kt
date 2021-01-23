package com.sprout.ui.fragment.main

import android.os.Bundle
import com.sprout.viewmodel.home.MyViewModel
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentMyBinding

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_my
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}