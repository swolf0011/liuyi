package com.abcly.swolf.mediacodecapp

import android.annotation.SuppressLint
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
object NYPermissionUtil {
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
    @SuppressLint("ObsoleteSdkInt")
    fun checkPermission(context: Context, permissions: Array<out String>): Boolean {
        //int M = 23;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (str in permissions) {
            val check = ContextCompat.checkSelfPermission(context, str)
            if (check != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 检测权限并申请
     * @param activity
     * @param permissions
     * @param requestCode
     * @return
     */
    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        //没有权限，请求权限
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }


    /**
     * 适配Android 30
     * @return Boolean
     */
    fun hasExternalStorageManagerPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return true
        return Environment.isExternalStorageManager()
    }

    /**
     * 是否拥有读写权限（适配Android 33）
     * @param context Context
     * @return Boolean
     */
    fun hasReadAndWritePermission(context: Context): Boolean {
        // int R = 30;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val externalStorageManager = Environment.isExternalStorageManager()
            if (externalStorageManager) return true
        }
        //TIRAMISU = 33;
        val hasPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(
                context,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            checkPermission(
                context,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
        return hasPermissions
    }


    /**
     * Android11让用户使用全部文件可以操作权限  int R = 30;
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun externalStorageManager(activity: Activity) {
        try {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                activity.startActivityForResult(intent, ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *
     * @param context Context
     * @param requestCode Int
     * @return Boolean
     */
    fun checkExternalStoragePermission(context: Context): Boolean {
        //int M = 23;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val permissions = arrayOf<String>(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        for (str in permissions) {
            val check = ContextCompat.checkSelfPermission(context, str)
            if (check != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *
     * @param activity Activity
     * @param requestCode Int
     * @return Boolean
     */
    fun requestExternalStoragePermissions(activity: Activity, requestCode: Int) {
        //int M = 23;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val permissions = arrayOf<String>(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        //没有权限，请求权限
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }


}