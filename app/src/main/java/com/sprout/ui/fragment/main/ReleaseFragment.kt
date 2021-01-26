package com.sprout.ui.fragment.main

import android.os.Bundle
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.nav
import com.sprout.databinding.FragmentReleaseBinding
import com.sprout.viewmodel.home.ReleaseViewModel

class ReleaseFragment :BaseFragment<ReleaseViewModel,FragmentReleaseBinding>(){
    override fun layoutId(): Int {
        return R.layout.fragment_release
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}