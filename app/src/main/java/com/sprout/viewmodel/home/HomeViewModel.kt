package com.sprout.viewmodel.home

import androidx.fragment.app.Fragment
import com.sprout.ui.fragment.homesub.FollowFragment
import com.sprout.ui.fragment.homesub.RecommendFragment
import com.sprout.ui.fragment.homesub.SameCityFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val tabList = arrayListOf<String>("同城","关注","推荐")
    var fragments = arrayListOf<Fragment>()

    fun fragmentInit(){
        fragments.add(SameCityFragment())
        fragments.add(FollowFragment())
        fragments.add(RecommendFragment())
    }



}