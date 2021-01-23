


package com.sprout.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.widget.SimpleAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.setLocationPermissions
import com.sprout.app.weight.pic.GlideEngine
import com.sprout.databinding.FragmentLabelBinding
import com.sprout.ui.adapter.BannerLabelAdapter
import com.sprout.viewmodel.LabelViewModel
import com.zhpan.bannerview.BannerViewPager
import me.hgj.jetpackmvvm.ext.util.TAG

class LabelFragment : BaseFragment<LabelViewModel, FragmentLabelBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_label
    }

    override fun initView(savedInstanceState: Bundle?) {

        mViewPager = mDatabind.bannerLabel as BannerViewPager<String>

        selectPirture()
    }
    private lateinit var mViewPager: BannerViewPager<String>


    fun selectPirture() {


        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofAll())
            .loadImageEngine(GlideEngine().createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: List<LocalMedia?>) {
                    // 结果回调

                    val list = result.mapTo(arrayListOf(), { it!!.realPath })

                    setLocationPermissions()
                    Log.e(TAG, "onResult: ${result[0]!!.realPath}" )

                    mViewPager.apply {

                        adapter = BannerLabelAdapter()
                        setLifecycleRegistry(lifecycle)
                    }.create(list)

                }

                override fun onCancel() {
                    // 取消
                }
            })
    }
}