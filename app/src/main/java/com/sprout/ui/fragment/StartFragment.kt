package com.sprout.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import com.sprout.viewmodel.StartViewModel
import com.sprout.R
import com.sprout.databinding.FragmentStartBinding
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.nav
import com.sprout.app.ext.navigateNoRepeat
import com.sprout.app.ext.setCameraPermissions
import com.sprout.app.ext.setLocationPermissions
import com.sprout.app.util.CacheUtil
import kotlinx.coroutines.delay
import java.util.*

class StartFragment : BaseFragment<StartViewModel, FragmentStartBinding>() {

    override fun layoutId(): Int {
        return R.layout.fragment_start
    }

    companion object {

        /**
         * 跳转到登录Fragment
         */
        fun jumpLoginFragment(fragment: Fragment) {

            fragment.nav().navigateNoRepeat(
                R.id.action_navigation_start_to_navigation_login
            )

        }

        /**
         * 跳转到HomeFragment
         */
        fun jumpHomeFragment(fragment: Fragment) {

            fragment.nav().navigateNoRepeat(
                R.id.action_navigation_start_to_navigation_main
            )
        }


    }


    override fun initView(savdInstanceState: Bundle?) {



        setCameraPermissions()
        setLocationPermissions()


        /**
         * 判断是否登录，根据结果跳转
         */
        if (CacheUtil.isLogin()) {
            jumpFragment {
                jumpHomeFragment(it)
            }

        } else {
            jumpFragment {
                jumpLoginFragment(it)
            }

        }


    }

    /**
     * 延迟两秒跳转到对应的Fragment(使用handler().pos
     */
    fun jumpFragment(jump: (Fragment) -> Unit) {


        val timer = Timer()

        class MyTimerTask() : TimerTask() {
            override fun run() {
                jump(this@StartFragment)
            }
        }

        val timerTask = MyTimerTask()
        timer.schedule(timerTask, 2000)


    }


}