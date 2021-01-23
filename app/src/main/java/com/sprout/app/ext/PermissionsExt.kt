package com.sprout.app.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

fun Fragment.setCameraPermissions(){

    XXPermissions.with(this)
        .permission(Permission.CAMERA)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: List<String>, all: Boolean) {
                if (all) {
                    Toast.makeText(context, "获取拍照权限成功", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDenied(permissions: List<String>, never: Boolean) {
                if (never) {
                    Toast.makeText(context, "被永久拒绝授权，请手动授予拍照权限", Toast.LENGTH_SHORT).show()
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(activity, permissions)
                } else {
                    Toast.makeText(context, "获取拍照权限失败", Toast.LENGTH_SHORT).show()
                }
            }
        })

}

fun Fragment.setLocationPermissions(){

    XXPermissions.with(this)
        .permission(Permission.Group.LOCATION)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: List<String>, all: Boolean) {
                if (all) {
                    Toast.makeText(context, "获取定位权限成功", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDenied(permissions: List<String>, never: Boolean) {
                if (never) {
                    ToastUtils.showLong("被永久拒绝授权，请手动授予定位权限")
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(this@setLocationPermissions, permissions)
                    return
                }
                if (permissions.size == 1 && Permission.ACCESS_BACKGROUND_LOCATION.equals(
                        permissions[0]
                    )
                ) {
                    Toast.makeText(context, "没有授予后台定位权限，请您选择始终允许", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "获取定位权限失败", Toast.LENGTH_SHORT).show()
                }
            }
        })

}