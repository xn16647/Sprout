package com.sprout.ui.fragment.main

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.app.ext.*
import com.sprout.app.weight.pic.GlideEngine
import com.sprout.data.model.bean.LabelReleaseBean
import com.sprout.data.model.bean.Res
import com.sprout.databinding.FragmentReleaseBinding
import com.sprout.ui.adapter.ReleaseImgAdapter
import com.sprout.viewmodel.home.ReleaseViewModel
import kotlinx.android.synthetic.main.fragment_release.*
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.toJson
import okhttp3.MediaType
import okhttp3.RequestBody


class ReleaseFragment : BaseFragment<ReleaseViewModel, FragmentReleaseBinding>(),
    OnItemClickListener {

    var address: String? = null
    val channelid: Int = 0
    var lat: Double = 0.0
    var lng: Double = 0.0
    lateinit var mood: String
    lateinit var res: MutableList<Res>
    var themeid: Int = 0
    lateinit var title: String
    var type: Int = 0

    fun jump(type: Int) {

        nav().navigateNoRepeat(
            R.id.action_navigation_release_to_navigation_release_add,
            Bundle().apply {
                putInt("type", type)
            })

    }

    var isMore = false

    lateinit var pathList: ArrayList<String>
    lateinit var list: ArrayList<String>
    lateinit var firstList: List<String>

    val imgAdapter: ReleaseImgAdapter by lazy {
        ReleaseImgAdapter()
    }

    override fun layoutId(): Int {
        return R.layout.fragment_release
    }

    lateinit var releaseBean: LabelReleaseBean

    override fun initView(savedInstanceState: Bundle?) {

        nav().getResult(viewLifecycleOwner, {

            //根据TYPE推断ReleaseAddFragment回传的数据
            when (getInt("type", -1)) {
                0 -> {
                    mDatabind.textView5.text = getString("name")
                    themeid = getInt("id")
                }
                1 -> {
                    address = getString("address").toString()
                    mDatabind.textView6.text = address
                    lat = getDouble("lat")
                    lng = getDouble("lon")

                }
            }
        })



        mDatabind.onClick = ProxyClick()

        res = arguments?.getParcelableArrayList<Res>("resList")!!

        type = arguments?.getInt("releaseType")!!

        //根据type判断视频还是图片并作出对应的处理
        if (type == 1)
            mDatabind.recyclerReleaseImg.visibility = View.GONE
        else
            setImgList()


        res.forEach {
            Log.d(TAG, "initView: ${it.url}")
        }


        firstList = eventViewModel.imagePathsEvent.value!!

    }


    private fun setImgList() {


        list = eventViewModel.imagePathsEvent.value!!.toMutableList() as ArrayList<String>



        mDatabind.recyclerReleaseImg.layoutManager =
            activity.let { LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) }


        if (list?.size!! < 9) {
            pathList = arrayListOf()
            list.forEach {
                pathList.add(it)
            }
            pathList.add("")
            isMore = true

            imgAdapter.setNewInstance(pathList)
            mDatabind.recyclerReleaseImg.adapter = imgAdapter

        } else {
            imgAdapter.setNewInstance(list.toMutableList())
            mDatabind.recyclerReleaseImg.adapter = imgAdapter
            isMore = false
        }

        imgAdapter.setOnItemClickListener(this)

    }


    override fun createObserver() {
        super.createObserver()

        //阿里云回调
        mViewModel.run {

            aliYunList.observe(viewLifecycleOwner, Observer {
                it.forEach { res.add(Res(null, it)) }
            })


            release.observe(viewLifecycleOwner, Observer {

                Log.d(TAG, "createObserver: ${it.toString()}")


                parseState(it, {


                    if (it.errno != 0) {
                        showMessage("登录信息已过期，点击确定为您跳转到登录页面！", positiveAction = {
                            nav().navigateNoRepeat(
                                R.id.action_navigation_release_to_navigation_login,
                                Bundle().apply {
                                    putInt("type", 1)
                                })
                        })
                    } else {
                        Log.d(TAG, "createObserver: ${it.errmsg}")

                        showMessage("发布成功！", positiveAction = {
                            nav().navigateNoRepeat(R.id.action_navigation_release_to_navigation_main)
                        })
                    }


                }, {

                    showMessage("服务器炸了")


                })

            })


        }


    }

    //点击事件的处理
    inner class ProxyClick {

        fun onClick(view: View) {
            when (view.id) {
                R.id.image_release_close -> {
                    nav().navigateUp()
                    eventViewModel.imagePathsEvent.value = firstList
                }
                R.id.btn_release_address -> jump(1)
                R.id.btn_release_append_theme -> jump(0)
                R.id.txt_release_btn -> commit()
                R.id.txt_save_release -> save()
            }
        }

    }

    fun commit() {

        mood = edit_release_content.text.toString()

        title = edit_release_title.text.toString()

        if (mood.isEmpty()) {
            Toast.makeText(context, "内容不能为空！", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty()) {
            Toast.makeText(context, "标题不能为空！", Toast.LENGTH_SHORT).show()
            return
        }

        if (res.size == 0) {
            Toast.makeText(context, "图片不能为空！", Toast.LENGTH_SHORT).show()
            return
        }

        if (themeid == 0) {
            Toast.makeText(context, "您还未设置主题！", Toast.LENGTH_SHORT).show()
            return
        }

        if (address == null) {
            Toast.makeText(context, "您还未设置地址！", Toast.LENGTH_SHORT).show()
            return
        }

        val bean = LabelReleaseBean(address!!, channelid, lat, lng, mood, res, themeid, title, 0)

        val json = bean.toJson()
        val paramBody: RequestBody = RequestBody.create(
            MediaType.parse("application/json;charset=UTF-8"),
            json
        )
        Log.d(TAG, "commit: ${json}")

        mViewModel.release(paramBody)
    }

    fun save() {


    }

    //图片选择器
    fun selectPirture(max: Int) {


        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofAll())
            .maxSelectNum(max)
            .loadImageEngine(GlideEngine().createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: List<LocalMedia?>) {
                    // 结果回调

                    val pathList = (result.mapTo(arrayListOf(), { it!!.realPath }))
                    list.addAll(pathList)

                    eventViewModel.imagePathsEvent.value = list

                    //根据集合长度设置顶部页码


                    //将本地图片上传到阿里云，并返回url地址
                    mViewModel.upImgToAliyun(pathList)


                    setImgList()


                }


                override fun onCancel() {
                    // 取消
                }
            })
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // 这种弹窗从 1.0.0版本开始实现了优雅的手势交互和智能嵌套滚动


        if (isMore && position == list.size) {

            selectPirture(9 - list.size)

        } else {
            XPopup.Builder(activity)
                .asBottomList(
                    "", arrayOf("再次编辑", "删除图片", "取消")
                ) { position2, text ->
                    when (position2) {
                        0 -> {
                            nav().navigateUpNoRepeat(Bundle().apply {
                                putStringArrayList("imgList", list)
                            })
                        }

                        1 -> {
                            list.removeAt(position)
                            eventViewModel.imagePathsEvent.value = list
                            setImgList()
                            res.removeAt(position)
                        }
                    }
                }
                .show()
        }


    }

}