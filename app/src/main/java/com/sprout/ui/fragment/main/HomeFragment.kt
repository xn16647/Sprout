package com.sprout.ui.fragment.main

import android.os.Bundle
import com.sprout.viewmodel.home.HomeViewModel
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.bindViewPager2
import com.sprout.app.ext.init
import com.sprout.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        //初始化fragmentList
        mViewModel.fragmentInit()
        //初始化viewpager2
        mDatabind.vp2Home.init(this, fragments = mViewModel.fragments).offscreenPageLimit =
            mViewModel.fragments.size

        //初始化指示器
        mDatabind.magicIndicatorHome.bindViewPager2(mDatabind.vp2Home, mStringList = mViewModel.tabList)

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
    }


}