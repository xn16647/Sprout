package com.sprout.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.initMain
import com.sprout.app.ext.nav
import com.sprout.app.ext.navigateNoRepeat
import com.sprout.app.weight.pic.GlideEngine
import com.sprout.databinding.FragmentMainBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.TAG


class MainFragment : BaseFragment<BaseViewModel, FragmentMainBinding>(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    override fun layoutId(): Int {
        return R.layout.fragment_main
    }


    companion object {

        fun jump(fragment: Fragment) {

            fragment.nav().navigateNoRepeat(R.id.action_navigation_main_to_navigation_label)

        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        //初始化viewpager2
        mDatabind.vp2Main.initMain(this)
        mDatabind.imageMainMore.requestFocus()

        mDatabind.navView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            //首页
            R.id.navigation_home -> mDatabind.vp2Main.setCurrentItem(0, false)

            //
            R.id.navigation_discover -> mDatabind.vp2Main.setCurrentItem(1, false)

            //消息页
            R.id.navigation_message -> mDatabind.vp2Main.setCurrentItem(3, false)

            //发布页
            R.id.navigation_more -> jump(this)

            //我的页面
            R.id.navigation_mine -> mDatabind.vp2Main.setCurrentItem(4, false)
        }

        return true

    }




}