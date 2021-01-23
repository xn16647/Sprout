package com.sprout.ui.fragment.main

import android.os.Bundle
import com.sprout.viewmodel.home.DiscoverViewModel
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentDiscoverBinding

class DiscoverFragment : BaseFragment<DiscoverViewModel, FragmentDiscoverBinding>(){
    override fun layoutId(): Int {
      return  R.layout.fragment_discover
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}