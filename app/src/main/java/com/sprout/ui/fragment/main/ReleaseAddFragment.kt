package com.sprout.ui.fragment.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.navigateUpNoRepeat
import com.sprout.data.model.bean.LocationInfo
import com.sprout.data.model.bean.ThemeBen
import com.sprout.databinding.AdapterReleaseAddThemeBinding
import com.sprout.databinding.FragmentAddReleaseBinding
import com.sprout.ui.adapter.ReleaseAddAdapter
import com.sprout.viewmodel.home.ReleaseAddViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState

class ReleaseAddFragment : BaseFragment<ReleaseAddViewModel, FragmentAddReleaseBinding>(),
    OnItemClickListener {

    val locationClient by lazy {
         AMapLocationClient(activity)
    }

    //位置信息的Adapter
    private val locationAdapter: ReleaseAddAdapter by lazy {
        ReleaseAddAdapter()
    }

    //位置信息的Adapter
    private val releaseAddAdapter: ReleaseAddThemeAdapter by lazy {
        ReleaseAddThemeAdapter()
    }

    //定位集合
    private lateinit var locationList: ArrayList<LocationInfo>
    private lateinit var themeList: ArrayList<ThemeBen>

    override fun layoutId(): Int {
        return R.layout.fragment_add_release
    }

    override fun initView(savedInstanceState: Bundle?) {



        val type = arguments?.getInt("type")

        when (type) {
            //0代表主题
            0 -> {
                mViewModel.getTheme()
            }
            //1代表地址
            1 -> {
                mViewModel.openLabelLocation(locationClient)
            }
        }

    }

    override fun createObserver() {
        super.createObserver()

        mViewModel.run {

            //开启定位
            mViewModel.amapLocation.observe(viewLifecycleOwner, Observer {

                mDatabind.txtReleaseAddCityname.apply {
                    visibility = View.VISIBLE
                    text = it.city
                }

                mViewModel.setSearchApi(it.latitude, it.longitude)

            })

            //位置
            mViewModel.locationInfoList.observe(viewLifecycleOwner, Observer {

                locationList = it

                locationAdapter.setNewInstance(it)
                locationAdapter.setOnItemClickListener(this@ReleaseAddFragment)
                mDatabind.txtReleaseAddRecycler.apply {
                    layoutManager = LinearLayoutManager(context)

                    adapter = locationAdapter
                }

                locationClient.stopLocation()
            })

            //主题
            mViewModel.themeBean.observe(viewLifecycleOwner, Observer {

                parseState(it, {

                    themeList = it

                    releaseAddAdapter.setNewInstance(it)
                    releaseAddAdapter.setOnItemClickListener(this@ReleaseAddFragment)
                    mDatabind.txtReleaseAddRecycler.apply {
                        layoutManager = LinearLayoutManager(context)

                        adapter = releaseAddAdapter
                    }

                })

            })

        }


    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.line_release_add_theme -> {
                nav().navigateUpNoRepeat(Bundle().apply {
                    putInt("type", 0)
                    putInt("id", themeList[position].id)
                    putString("name", themeList[position].name)
                })
            }
            R.id.cons_release_add_location -> {
                nav().navigateUpNoRepeat(Bundle().apply {
                    putInt("type", 1)
                    putString("address", locationList[position].address)
                    putDouble("lat", locationList[position].Latitude)
                    putDouble("lon", locationList[position].Longitude)
                })
            }
        }
    }

    inner class ReleaseAddThemeAdapter :
        BaseQuickAdapter<ThemeBen, BaseDataBindingHolder<AdapterReleaseAddThemeBinding>>(R.layout.adapter_release_add_theme) {
        override fun convert(
            holder: BaseDataBindingHolder<AdapterReleaseAddThemeBinding>,
            item: ThemeBen
        ) {
            holder.dataBinding?.theme = item
        }

    }

}