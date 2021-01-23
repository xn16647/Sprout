package com.sprout.ui.fragment.main

import android.os.Bundle
import androidx.lifecycle.Observer
import com.sprout.viewmodel.home.MessageViewModel
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentMessageBinding

class MessageFragment : BaseFragment<MessageViewModel, FragmentMessageBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_message
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {
        super.createObserver()

        mViewModel.text.observe(
            viewLifecycleOwner, Observer {
                mDatabind.textNotifications.text = it
            }
        )

    }

}