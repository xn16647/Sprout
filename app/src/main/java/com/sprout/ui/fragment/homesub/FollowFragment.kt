package com.sprout.ui.fragment.homesub

import android.os.Bundle
import com.sprout.R
import com.sprout.viewmodel.subhome.FollowViewModel
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentFollowBinding

class FollowFragment: BaseFragment<FollowViewModel, FragmentFollowBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_follow
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
        super.lazyLoadData()

    }
}