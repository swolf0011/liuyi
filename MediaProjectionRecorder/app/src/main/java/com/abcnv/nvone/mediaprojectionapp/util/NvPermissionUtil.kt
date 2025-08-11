package com.abcnv.nvone.mediaprojectionapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class NvPermissionUtil {

    val write_external = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val read_external = Manifest.permission.READ_EXTERNAL_STORAGE
    val foreground_service = Manifest.permission.FOREGROUND_SERVICE
    val PS_EXTERNAL = arrayOf(write_external, read_external, foreground_service)


    private var activityResultLauncher: ActivityResultLauncher<Array<String>>? = null

    /**
     * 检查存储权限
     */
    fun checkPermission(activity: Context, ps: Array<String>): Boolean {
        ps.forEach {
            val i = ContextCompat.checkSelfPermission(activity, it)
            if (i != PackageManager.PERMISSION_GRANTED) {
                val msg = "请先把给与权限，去《设置/应用/权限》"
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    /**
     * 申请存储权限
     */
    fun getPermissionToOnCreate(
        activity: AppCompatActivity,
        ps: Array<String>,
        callback: (Boolean) -> Unit
    ) {
        val requestMultiplePermissions = ActivityResultContracts.RequestMultiplePermissions()
        requestMultiplePermissions.createIntent(activity, ps)
        activityResultLauncher =
            activity.registerForActivityResult(requestMultiplePermissions) {
                var result = true
                it.keys.forEach { key ->
                    if (it[key] == null || it[key] == false) {
                        result = false
                    }
                }
                callback.invoke(result)
            }
    }

    fun launch(ps: Array<String>) {
        activityResultLauncher?.launch(ps)
    }

}