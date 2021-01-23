package com.sprout.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sprout.R
import com.sprout.app.base.BaseActivity
import com.sprout.app.ext.setNavigationBar
import com.sprout.databinding.ActivityMainBinding
import com.sprout.viewmodel.MainActivityViewModel
import me.hgj.jetpackmvvm.navigation.NavHostFragment


class MainActivity : BaseActivity<MainActivityViewModel,ActivityMainBinding>() {





    override fun layoutId(): Int {

        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        setNavigationBar(false,this)

    }


}