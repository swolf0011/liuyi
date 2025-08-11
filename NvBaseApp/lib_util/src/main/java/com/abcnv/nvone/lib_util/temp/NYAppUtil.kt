package com.abcly.swolf.lib_util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import java.util.*

/**
 * apk信息工具
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * Created by LiuYi-15973602714
 */
object NYAppUtil {
    /**
     * 检测网络是否通
     */
    @Synchronized
    fun checkNetStatus(context: Context): Boolean {
        var netStatus = false
        try {
            val connManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connManager.activeNetworkInfo
            if (connManager.activeNetworkInfo != null) {
                netStatus = connManager.activeNetworkInfo!!.isConnected
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            netStatus = false
        }
        return netStatus
    }

    /**
     * 判断app是否在前台
     */
    fun isAppForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        appProcesses.forEach {
            if (it.processName == context.packageName) {
                return it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }


    /**
     * 重新设置语言,否则分屏时fragment会变回中文
     * @param context Context
     * @param isChina Boolean
     */
    fun setAppLanguage(context: Context, isChina: Boolean) {
        val locale = if (isChina) {
            Locale.CHINA
        } else {
            Locale.ENGLISH
        }
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        context.createConfigurationContext(configuration)
        //实测，updateConfiguration这个方法虽然很多博主说是版本不适用
        //但是我的生产环境androidX+Android Q环境下，必须加上这一句，才可以通过重启App来切换语言
        resources.updateConfiguration(configuration, metrics)
    }

    fun getVersion(activity: Activity): Long {
        val pkg = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0)
        pkg?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return pkg.longVersionCode
            }
            return pkg.versionCode + 0L
        }
        return 0L
    }

    /**
     * 获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    fun getMacAddress(context: Context): String? {
        val wifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return wifiInfo.macAddress
    }

    /**
     * 获取序列号
     *
     * @return
     */
    private fun getSerialNumber(): String? {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return serial
    }

    /**
     * 获取分辨率Width*Height
     */
    fun getResolution(activity: Activity): DisplayMetrics? {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val s = "" + dm.widthPixels + "*" + dm.heightPixels
        return dm
    }

    /**
     * 获取安装的apks
     */
    fun getInstallApks(context: Context): List<PackageInfo?>? {
        val packageManager = context.packageManager
        var mAllPackages = packageManager.getInstalledPackages(0)
        if (mAllPackages == null) {
            mAllPackages = ArrayList()
        }
        return mAllPackages
    }


    /**
     * 获取已安装apk信息，versionName版本名，versionCode版本号等。
     */
    fun getInstallAPKInfo(context: Context, packageName: String): PackageInfo? {
        val pm = context.packageManager
        try {
            return pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取未安装apk信息，versionName版本名，versionCode版本号等。
     *
     * @param context
     * @param archiveFilePath Environment.getExternalStorageDirectory()+"/"+"TestB.apk"
     * @return
     */
    fun getUninstallAPKInfo(
        context: Context,
        archiveFilePath: String
    ): PackageInfo? {
        val pm = context.packageManager
        return pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES)
    }

}