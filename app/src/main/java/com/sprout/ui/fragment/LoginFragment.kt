package com.sprout.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.sprout.R
import com.sprout.app.App
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.nav
import com.sprout.app.ext.navigateNoRepeat
import com.sprout.app.ext.showMessage
import com.sprout.app.util.CacheUtil
import com.sprout.data.model.bean.Poi
import com.sprout.databinding.FragmentLoginBinding
import com.sprout.viewmodel.LoginViewModel
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.TAG
import me.hgj.jetpackmvvm.ext.util.loge
import kotlin.math.log


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(), AMapLocationListener {


    lateinit var mLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption
    private var isRegister = false

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    companion object {

        fun jumpFragment(fragment: Fragment) {

            fragment.nav().navigateNoRepeat(
                R.id.action_navigation_login_to_navigation_main
            )

        }

    }

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.loginClick = ProxyClick()
        location()

    }

    override fun createObserver() {
        super.createObserver()


        //加载数据
        mViewModel.run {


            url.observe(viewLifecycleOwner, Observer {
                mDatabind.videoPlayer.setUp(it, true, "")
                mDatabind.videoPlayer.setLooping(true)
                mDatabind.videoPlayer.startPlayLogic()
            })


            registerInfo.observe(
                viewLifecycleOwner,
                Observer {
                    parseState(it, {


                        Log.e(TAG, "createObserver: $isRegister")
                        Log.e(TAG, "createObserver: $it")



                        if (isRegister) {
                            if (it.userInfo == null) {
                                showMessage("用户名已注册！")
                            } else {
                                showMessage("注册成功！请登录", positiveAction = {
                                    ProxyClick().register()
                                })
                            }
                        } else {
                            if (it.code == 200) {
                                CacheUtil.setUser(userResponse = it.userInfo)
                                CacheUtil.setToken(it.token)

                                showMessage("登录成功！", positiveAction = {
                                    jumpFragment(this@LoginFragment)
                                })
                            } else {
                                showMessage("登录失败！请检查账号密码是否正确！")
                            }
                        }


                    }, {
                        showMessage(it.errorMsg)
                    })
                }
            )
        }


    }


    override fun lazyLoadData() {
        super.lazyLoadData()




    }


    inner class ProxyClick {

        //注册和登录页面的切换
        fun register() {

            if (isRegister) {

                mDatabind.txtRegisBtn.text = "注册"
                mDatabind.txtLoginCommitBtn.text = "登录"
            } else {
                mDatabind.txtRegisBtn.text = "登录"
                mDatabind.txtLoginCommitBtn.text = "注册"
            }

            isRegister = !isRegister
        }

        // 注册和登录接口的提交
        fun loginBtnCommit() {


            username = mDatabind.editLoginPhoneNumber.text.toString()
            userPsw = mDatabind.editLoginPsw.text.toString()
            when {
                username.isEmpty() -> showMessage("请填写账号")
                userPsw.isEmpty() -> showMessage("请填写密码")
                else -> if (isRegister) {
                    location()
                } else {
                    mViewModel.login(username, userPsw)
                }
            }


        }


    }

    lateinit var username: String
    lateinit var userPsw: String


    /*开启定位*/
    private fun location() {
        //初始化定位
        mLocationClient =
            AMapLocationClient(context)
        //设置定位回调监听
        mLocationClient.setLocationListener(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true)
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }

    @SuppressLint("HardwareIds")
    override fun onLocationChanged(p0: AMapLocation?) {
        Log.e(TAG, "onLocationChanged: ${p0?.errorInfo}")
        val lat = p0?.getLatitude();//获取纬度
        val lon = p0?.getLongitude();//获取经度

        /**
         * AndroidId
         */
        val m_szAndroidID: String = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID
        )



        mViewModel.register(
            username,
            userPsw,
            imei = m_szAndroidID,
            lat = lat.toString(),
            lng = lon.toString()
        )

    }


}