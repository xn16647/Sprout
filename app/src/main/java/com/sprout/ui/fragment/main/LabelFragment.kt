package com.sprout.ui.fragment.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.AMapLocationClient
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.getResult
import com.sprout.app.ext.navigateNoRepeat
import com.sprout.app.util.CacheUtil
import com.sprout.app.util.UIUtils
import com.sprout.app.weight.photodraweeview.ITagBean
import com.sprout.app.weight.photodraweeview.PictureTagFrameLayout
import com.sprout.app.weight.photodraweeview.TagBean
import com.sprout.app.weight.pic.GlideEngine
import com.sprout.data.model.bean.*
import com.sprout.databinding.FragmentLabelBinding
import com.sprout.ui.adapter.*
import com.sprout.ui.adapter.label.*
import com.sprout.viewmodel.LabelViewModel
import kotlinx.android.synthetic.main.fragment_label.*
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logd


class LabelFragment : BaseFragment<LabelViewModel, FragmentLabelBinding>(),
    PictureTagFrameLayout.ITagLayoutCallBack, OnItemClickListener {

    fun jump() {



        nav().navigateNoRepeat(R.id.action_navigation_label_to_navigation_release, Bundle().apply {
            putParcelableArrayList("resList", resList)
            putInt("releaseType",releaseType)
        })
    }


    override fun layoutId(): Int {
        return R.layout.fragment_label
    }


    //定位对象
    val locationClient by lazy {
        AMapLocationClient(activity)
    }

    //发布TYPE
    var releaseType:Int = 0

    //banner Adapter
    private var mImageAdapter: ImagePagerAdapterForPublish? = null

    //根据图片地址存储标签
    private var mTagBeansMap: Map<String, List<ITagBean>>? = null

    //品牌TAG的Adapter
    private val brandAdapter: LabelBrandAdapter by lazy {
        LabelBrandAdapter()
    }

    //商品TAG的Adapter
    private val goodsAdapter: LabelGoodsAdapter by lazy {
        LabelGoodsAdapter()
    }

    //定位TAG的Adapter
    private val locationAdapter: LabelLocationAdapter by lazy {
        LabelLocationAdapter()
    }

    //用于存储阿里云图片地址的集合
    lateinit var urlList: ArrayList<String>

    //记录上一个初始化的tag
    lateinit var lastTagText: TextView

    //最近标签adapter
    val recentTagsAdapter: RecentTagsAdapter by lazy {
        RecentTagsAdapter()
    }

    //品牌集合
    private lateinit var brandList: MutableList<LabelTag>

    //图片集合
    private lateinit var goodsList: ArrayList<LabelGoodsData>

    //定位集合
    private lateinit var locationList: ArrayList<LocationInfo>

    //经纬度
    var lat = 0.0
    var lon = 0.0

    //搜索TYPE
    var searchType = 0

    //搜索需要存储的原始数据集合
    val brandList2: MutableList<LabelTag> = arrayListOf()
    val goodsList2: MutableList<LabelGoodsData> = arrayListOf()
    val locationList2: MutableList<LocationInfo> = arrayListOf()
    var resList: ArrayList<Res> = arrayListOf()

    override fun initView(savedInstanceState: Bundle?) {

        mViewPager = mDatabind.bannerLabel

        initBannerPager()
        selectPirture()


        //从发布页返回的数据
        nav().getResult(viewLifecycleOwner, {
            val imgList = getStringArrayList("imgList")

            if (imgList != list) {

                list = imgList!!
                //根据集合长度设置顶部页码
                mDatabind.txtLabelBannerIndex.text = "1/${list.size}"


                //将本地图片上传到阿里云，并返回url地址
                mViewModel.upImgToAliyun(list,"image/jpeg")
                mViewModel.getLabelBrandList(1)
                mTagBeansMap =  mImageAdapter!!.tagBeansMap


                update()


            }

        })


        //开启定位
        mViewModel.openLabelLocation(locationClient)

        Fresco.initialize(context)
        //初始化bannerAdapter
        mImageAdapter = ImagePagerAdapterForPublish(activity, this)
        mViewPager.adapter = mImageAdapter

        //动态更换顶部页码
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                mDatabind.txtLabelBannerIndex.text = "${position + 1}/${list.size}"

            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        //记录第一个变黑字体变大的tab
        lastTagText = mDatabind.txtTagBrand
        //绑定点击事件
        mDatabind.onClick = Proxy()
        //标签集合设置为横向
        mDatabind.recyclerRecentTag.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //加载最近标签页的adapter
        setTagAdapter()


        //搜索
        mDatabind.editLabelInputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                when (searchType) {
                    0 -> {

                        if (s!!.isEmpty()) {
                            brandAdapter.setList(brandList2)
                            brandAdapter.notifyDataSetChanged()
                        }


                        brandAdapter.setList(brandList.dropLastWhile {
                            !it.name.contains(s.toString())
                        })
                        brandAdapter.notifyDataSetChanged()

                    }
                    1 -> {

                        if (s!!.isEmpty()) {
                            goodsAdapter.setList(goodsList2)
                            goodsAdapter.notifyDataSetChanged()
                        }


                        goodsAdapter.setList(goodsList2.dropLastWhile {
                            !it.name.contains(s.toString())
                        })
                        goodsAdapter.notifyDataSetChanged()

                    }

                    3 -> {
                        if (s!!.isEmpty()) {
                            locationAdapter.setList(locationList2)
                            locationAdapter.notifyDataSetChanged()
                        }


                        locationAdapter.setList(locationList2.dropLastWhile {
                            !it.address2.contains(s.toString())
                        })
                        locationAdapter.notifyDataSetChanged()
                    }
                }

            }

        })


    }

    //通过mmkt存储最近标签，详情可去CacheUtil查看
    private fun setTagAdapter() {

        val tags = CacheUtil.getTag()
        if (tags != null) {
            val tagArr = tags.split(",")
            recentTagsAdapter.setNewInstance(tagArr.toMutableList())
            mDatabind.recyclerRecentTag.adapter = recentTagsAdapter
        }
    }


    //更新图片adapter
    private fun update() {
        val mMediaTotal: MutableList<IChooserModel> = ArrayList<IChooserModel>()
        for (i in list.indices) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(list.get(i), options)
            val chooserModel = ChooserModel()
            chooserModel.filePath = list.get(i)
            chooserModel.setHeight(options.outHeight)
            chooserModel.setWidth(options.outWidth)
            mMediaTotal.add(chooserModel)
        }
        mImageAdapter!!.setData(mMediaTotal, mTagBeansMap, UIUtils.dip2Px(context, 20f).toInt())
    }


    //根据点击的标签添加到图片中
    fun onTagSelected(tagBean: ITagBean?) {


        if (tagBean != null && mImageAdapter != null) {

            CacheUtil.setTag(tagBean.tagName, tagBean.type.toString())
            setTagAdapter()

            val itemView = mImageAdapter!!.primaryItem
            if (itemView is PictureTagFrameLayout) {
                tagBean.sx = 0.3f
                tagBean.sy = 0.5f
                itemView.addItem(tagBean)
            }
        }

    }


    override fun createObserver() {
        super.createObserver()

        mViewModel.run {


            //品牌列表数据处理
            mViewModel.labelBrandData.observe(viewLifecycleOwner, Observer {
                parseState(it, { it ->


                    Log.e(TAG, "createObserver: $it")

                    brandList = it.data.toMutableList()
                    if (brandList2.size == 0) {
                        brandList.forEach {
                            brandList2.add(it)
                        }
                    }
                    brandAdapter.setNewInstance(brandList)
                    brandAdapter.setOnItemClickListener(this@LabelFragment)
                    mDatabind.recyclerLabelChannel.apply {

                        layoutManager = LinearLayoutManager(context)

                        adapter = brandAdapter

                    }
                })
            })

            //商品列表的数据处理
            mViewModel.labelGoodsData.observe(viewLifecycleOwner, Observer {
                parseState(it, {
                    goodsList = it.data
                    if (goodsList2.size == 0) {
                        goodsList.forEach {
                            goodsList2.add(it)
                        }
                    }

                    goodsAdapter.setNewInstance(it.data.toMutableList())
                    goodsAdapter.setOnItemClickListener(this@LabelFragment)
                    mDatabind.recyclerLabelChannel.apply {
                        layoutManager = LinearLayoutManager(context)

                        adapter = goodsAdapter
                    }
                })
            })


            //上传图片到阿里云的数据处理
            mViewModel.aliYunList.observe(viewLifecycleOwner, Observer {

                dismissLoading()
                urlList = it

                for (s in it) {
                    s.logd("Tag")
                }


            })

            //开启定位的回调处理
            mViewModel.amapLocation.observe(viewLifecycleOwner, Observer {
                lat = it.latitude
                lon = it.longitude
            })


            //检索周边信息的回调处理
            mViewModel.locationInfoList.observe(viewLifecycleOwner, Observer {
                locationList = it


                if (locationList2.size == 0) {
                    locationList.forEach {
                        locationList2.add(it)
                    }
                }


                locationAdapter.setNewInstance(it)
                locationAdapter.setOnItemClickListener(this@LabelFragment)
                mDatabind.recyclerLabelChannel.apply {
                    layoutManager = LinearLayoutManager(context)

                    adapter = locationAdapter
                }

                locationClient.stopLocation()

            })

        }


    }


    //初始化banner属性

    private fun initBannerPager() {

        mViewPager.apply {


        }
    }

    private lateinit var mViewPager: ViewPager


    lateinit var list: ArrayList<String>

    override fun onPause() {
        super.onPause()
        "onpause".logd("Label")
        video_label.onVideoPause()
    }


    override fun onResume() {
        super.onResume()
        video_label.onVideoResume()
    }

    override fun onStop() {
        super.onStop()
        GSYVideoManager.releaseAllVideos()
    }

    //图片选择器
    fun selectPirture() {


        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofAll())
            .loadImageEngine(GlideEngine().createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: List<LocalMedia?>) {
                    // 结果回调

                    list = result.mapTo(arrayListOf(), { it!!.realPath })

                    eventViewModel.imagePathsEvent.value = list




                    //根据集合长度设置顶部页码
                    mDatabind.txtLabelBannerIndex.text = "1/${list.size}"


                    val type = result[0]!!.mimeType
                    showLoading()

                    type.logd("Type")

                    if (type=="video/mp4") {
                        mDatabind.group.visibility = View.GONE
                        mDatabind.videoLabel.visibility = View.VISIBLE


                        mDatabind.videoLabel.setUp(result[0]!!.path,false,"标题啊")


                    }else{
                        update()
                        mViewModel.getLabelBrandList(1)
                    }


                    //将本地图片上传到阿里云，并返回url地址
                    mViewModel.upImgToAliyun(list,type)




                }


                override fun onCancel() {
                    // 取消
                }
            })
    }


    //点击事件的处理
    inner class Proxy {


        fun txtTag(view: View) {

            when (view.id) {
                R.id.txt_tag_brand -> {
                    setTagTheme(mDatabind.txtTagBrand)
                    mViewModel.getLabelBrandList(1)
                    searchType = 0
                }
                R.id.txt_tag_goods -> {
                    setTagTheme(mDatabind.txtTagGoods)
                    mViewModel.getLabelGoodsList(1)
                    searchType = 1
                }
                R.id.txt_tag_location -> {
                    setTagTheme(mDatabind.txtTagLocation)

                    mViewModel.setSearchApi(lat, lon)
                    searchType = 3

                }
                R.id.txt_tag_user -> setTagTheme(mDatabind.txtTagUser)
                R.id.txt_label_next -> getTagPoint()
            }

        }


    }

    private fun getTagPoint() {

        val tagBeans = mImageAdapter!!.tagBeansMap

        if (tagBeans != null) {

            val imgTop = mViewPager.top
            val right = mViewPager.right


            for (i in 0 until list.size) {
                val tagList = ArrayList<Tag>()
                tagBeans.get(list.get(i))?.forEach {

                    val tagY = imgTop * it.sy + imgTop
                    val tagX = right * it.sx

                    tagList += Tag(
                        -1,
                        lat,
                        lon,
                        it.tagName,
                        it.type,
                        tagX,
                        tagY
                    )

                    Log.d(TAG, "tagY: ${imgTop * it.sy + imgTop}")
                    Log.d(TAG, "tagX: ${right * it.sx}")

                }
                resList.add(Res(tagList, url = urlList.get(i)))
            }

            jump()


        } else {
            Log.e(TAG, "getTagPoint: mTagBeanMap is Null")

            if (mDatabind.videoLabel.visibility == View.VISIBLE) {
                releaseType = 1
                resList.add(Res(null, url = urlList.get(0)))
                jump()
            }

        }
    }


    //根据点击事件动态更换4个Tag的状态
    private fun setTagTheme(txtTag: TextView) {


        if (lastTagText != txtTag) {
            txtTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            txtTag.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            txtTag.setTextColor(resources.getColor(R.color.black))
            lastTagText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            lastTagText.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            lastTagText.setTextColor(resources.getColor(R.color.colorBlack666))


            lastTagText = txtTag


        }

    }


    override fun onSingleClick(x: Float, y: Float) {
    }

    override fun onTagViewMoving() {
    }

    override fun onTagViewStopMoving() {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.cons_label_brand -> onTagSelected(
                TagBean(
                    0,
                    brandList.get(position).name,
                    "",
                    -1,
                    -1f,
                    -1f
                )
            )
            R.id.cons_label_goods -> onTagSelected(
                TagBean(
                    1,
                    goodsList.get(position).name,
                    "",
                    -1,
                    -1f,
                    -1f
                )
            )
            R.id.cons_label_location -> onTagSelected(
                TagBean(
                    3,
                    locationList.get(position).address2,
                    "",
                    -1,
                    -1f,
                    -1f
                )
            )

        }
    }
}