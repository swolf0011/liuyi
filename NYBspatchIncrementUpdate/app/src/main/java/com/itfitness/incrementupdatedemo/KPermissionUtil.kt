package com.itfitness.incrementupdatedemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception

/**
 * Android6.0及以上申请权限
 */
object KPermissionUtil{
    //    public final int PERMISSION_requestCode = 1000;

    const val ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE = 39999

    /**
     * 判断系统api版本大于等于23才走权限检查  //int M = 23;
     */
    val isMNC: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


    /**
     * 检测权限
     * @param context
     * @param permissions
     * @return
     */
    fun checkSelfPermission(context: Context, permissions: Array<out String>): Boolean {
        //int M = 23;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val list = ArrayList<String>()
        for (str in permissions) {
            if (ContextCompat.checkSelfPermission(context, str) != PackageManager.PERMISSION_GRANTED) {
                list.add(str)
            }
        }
        return list.size == 0
    }

    /**
     * 适配Android 30
     * @return Boolean
     */
    fun hasExternalStorageManagerPermission(): Boolean {
        // int R = 30;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return true
        return Environment.isExternalStorageManager()
    }
//    /**
//     * 是否拥有读写权限（适配Android 33）
//     * @param context Context
//     * @return Boolean
//     */
//    fun hasReadAndWritePermission(context: Context):Boolean{
//        // int R = 30;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val externalStorageManager = Environment.isExternalStorageManager()
//            if (externalStorageManager) return true
//        }
//        //TIRAMISU = 33;
//        val hasPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            checkSelfPermission(
//                context,
//                arrayOf(
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_MEDIA_AUDIO,
//                    android.Manifest.permission.READ_MEDIA_IMAGES,
//                    android.Manifest.permission.READ_MEDIA_VIDEO
//                )
//            )
//        }else{
//            checkSelfPermission(
//                context,
//                arrayOf(
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            )
//        }
//        return hasPermissions
//    }
    /**
     * 检测权限并申请
     * @param activity
     * @param permissions
     * @return
     */
    fun checkSelfPermission2requestPermissions(activity: Activity, permissions: Array<String>, PERMISSION_requestCode: Int): Boolean {
        //int M = 23;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        var bool = true
        val list = ArrayList<String>()
        for (str in permissions) {
            if (ContextCompat.checkSelfPermission(activity, str) != PackageManager.PERMISSION_GRANTED) {
                list.add(str)
            }
        }
        if (list.size > 0) {
            val strs = arrayOfNulls<String>(list.size)
            for (i in strs.indices) {
                strs[i] = list[i]
            }
            //没有权限，请求权限
            ActivityCompat.requestPermissions(activity, strs, PERMISSION_requestCode)
            bool = false
        }
        return bool
    }

    /**
     * Android11让用户使用全部文件可以操作权限  int R = 30;
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun externalStorageManager(activity: Activity){
        try {
            if(!Environment.isExternalStorageManager()){
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                activity.startActivityForResult(intent, ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }





}