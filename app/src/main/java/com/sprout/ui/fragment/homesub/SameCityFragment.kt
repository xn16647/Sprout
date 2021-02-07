package com.sprout.ui.fragment.homesub

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sprout.R
import com.sprout.app.base.BaseFragment
import com.sprout.databinding.FragmentSameCityBinding
import com.sprout.ui.adapter.SameCityAdapter
import com.sprout.viewmodel.subhome.SameCityViewModel
import me.hgj.jetpackmvvm.ext.parseState

class SameCityFragment: BaseFragment<SameCityViewModel, FragmentSameCityBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_same_city
    }

    val adapter:SameCityAdapter by lazy {
        SameCityAdapter()
    }


    override fun initView(savedInstanceState: Bundle?) {

        mViewModel.getSameCityBean(2,1)

        mDatabind.recyclerSameCity.apply {

            layoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)

        }


    }

    override fun createObserver() {
        super.createObserver()

        mViewModel.run {

            sameCityBean.observe(viewLifecycleOwner, Observer {
                parseState(it,{

                    adapter.setNewInstance(it.data.toMutableList())

                    mDatabind.recyclerSameCity.adapter = adapter


                })
            })

        }

    }
}