package com.sprout.ui.fragment.homesub

import android.os.Bundle
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.bindReCommendViewPager2
import com.sprout.app.ext.init
import com.sprout.app.ext.initRecommend
import com.sprout.databinding.FragmentRecommendBinding
import com.sprout.viewmodel.subhome.RecommendViewModel

class RecommendFragment: BaseFragment<RecommendViewModel, FragmentRecommendBinding>() {
    override fun layoutId(): Int {

        return R.layout.fragment_recommend

    }

    override fun initView(savedInstanceState: Bundle?) {


        //加载Fragment数据
        mViewModel.fragmentInit()

        //初始化viewpager2设置适配器
        mDatabind.vp2Recommend.init(this,mViewModel.fragments)

        //初始化fragment指示器
        mDatabind.tabHomeRecommend.bindReCommendViewPager2(mDatabind.vp2Recommend,mViewModel.tapTitles)



    }
}