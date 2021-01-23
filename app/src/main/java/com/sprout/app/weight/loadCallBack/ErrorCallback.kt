package com.sprout.app.weight.loadCallBack

import com.kingja.loadsir.callback.Callback
import com.sprout.R


class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}