package com.sprout.app.weight.loadCallBack


import com.kingja.loadsir.callback.Callback
import com.sprout.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}