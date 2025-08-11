package com.abcnv.one.lib_cameraxutil


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * @Description: Android6.0及以上申请权限
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
object NvPermissionUtil {
    /**
     * 判断系统api版本大于等于23  //int M = 23 = android 6.0
     */
    val isM_23: Boolean
        @SuppressLint("ObsoleteSdkInt")
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**
     * 判断系统api版本大于等于30  //int R = 30= android 11
     */
    val isR_30: Boolean
        @SuppressLint("ObsoleteSdkInt")
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    /**
     * 判断系统api版本大于等于33  //int MTIRAMISU = 33 = android 13
     */
    val isTIRAMISU_33: Boolean
        @SuppressLint("ObsoleteSdkInt")
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

//    /**
//     * TIRAMISU >= 33 读写权限
//     * <uses-permission android:name="android.permission.READ_MEDIA_AUDIO"/>
//     * <uses-permission android:name="android.permission.READ_MEDIA_IMAGES"/>
//     * <uses-permission android:name="android.permission.READ_MEDIA_VIDEO"/>
//     */
//    @RequiresApi(33)
//    val permissionsWriteRead33 = arrayOf(
//        Manifest.permission.READ_MEDIA_AUDIO,
//        Manifest.permission.READ_MEDIA_IMAGES,
//        Manifest.permission.READ_MEDIA_VIDEO,
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.FOREGROUND_SERVICE
//    )
//
//    /**
//     * TIRAMISU < 33 读写权限
//     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
//     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
//     */
//    val permissionsWriteRead32 = arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.FOREGROUND_SERVICE
//    )

    /**
     * 摄像头权限
     * <uses-permission android:name="android.permission.CAMERA" />
     * <uses-feature android:name="android.hardware.camera" android:required="true" />
     */
    val permissionsCamera = arrayOf(
        Manifest.permission.CAMERA
    )

    /**
     * 权限申请，必须在onStart之前注册
     * @param activity AppCompatActivity
     * @param callback Function1<Boolean, Unit>
     * @return ActivityResultLauncher<Array<String>>  registerForActivityResult.launch(ps)
     */
    fun registerRequestMultiplePermissions(
        activity: AppCompatActivity,
        ps: Array<String>,
        callback: (Boolean) -> Unit
    ): ActivityResultLauncher<Array<String>> {
        val rmp = ActivityResultContracts.RequestMultiplePermissions()
        rmp.createIntent(activity, ps)
        val registerForActivityResult = activity.registerForActivityResult(rmp) { permissionsMap ->
            var result = true
            permissionsMap.keys.forEach {
                if (permissionsMap[it] != true) {
                    //权限全部获取后的操作
                    result = false
                }
            }
            //权限全部获取后的操作
            callback.invoke(result)
        }
//        registerForActivityResult.launch(ps)
        return registerForActivityResult
    }

    /**
     * Activity跳转，必须在onStart之前注册
     * @param activity AppCompatActivity
     * @param callback Function1<Intent, Unit>
     * @return ActivityResultLauncher<Intent>   registerForActivityResult.launch(Intent)
     */
    fun registerStartActivityForResult(
        activity: AppCompatActivity,
        callback: (Intent) -> Unit
    ): ActivityResultLauncher<Intent> {
        val safr = ActivityResultContracts.StartActivityForResult()
        val registerForActivityResult = activity.registerForActivityResult(safr) {
            if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                callback.invoke(it.data!!)
            }
        }
//        registerForActivityResult.launch(Intent)
        return registerForActivityResult
    }


    /**
     * <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
     * 判断是否有全部文件可以操作权限  Android11 int R = 30
     */
    fun isExternalStorageManager(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return true
        }
        return Environment.isExternalStorageManager()
    }

    /**
     * <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
     * 申请使用全部文件可以操作权限  Android11 int R = 30
     *
     * @param activityResultLauncher ActivityResultLauncher<Intent>
     * @param callback Function0<Unit> 有权限时的操作
     */
    fun externalStorageManager(activityResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        activityResultLauncher.launch(intent)
    }
}