package com.abcnv.nvone.lib_util

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface
import android.view.WindowManager

object NvDeviceUtil {


    /**
     * 设备是否为平板>=7寸
     */
    fun isPad(context: Context): Boolean {
        val display = getDisplay(context)
        val (deviceScreenWidth,deviceScreenHeight) = getDeviceScreenWidthHeight(display)
        val dm = DisplayMetrics()
        display.getMetrics(dm)
        val x = Math.pow((deviceScreenWidth / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((deviceScreenHeight / dm.ydpi).toDouble(), 2.0)
        // 屏幕尺寸
        val screenInches = Math.sqrt(x + y)
        // <=7尺寸则为Pad
        return screenInches >= 7.0
    }
    /**
     *
     * 获取WindowManager的Display实例
     * @param context:上下文
     */
    fun getDisplay(context:Context): Display {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            display?.let {
                return it
            }
        }
        return wm.defaultDisplay
    }
    /**
     * 获取设备的宽高。如：竖屏1600、2560；横屏2560、1600
     * var (deviceScreenWidth,deviceScreenHeight) = getDeviceScreenWidthHeight(context)
     * @param context:上下文
     * @return Pair<Float,Float>宽高
     */
    fun getDeviceScreenWidthHeight(display:Display):Pair<Int,Int>{
        var deviceScreenWidth =0
        var deviceScreenHeight = 0
        try{
            val cls_display = display::class.java
            //获得私有属性
            val mDisplayInfo_Field = cls_display.getDeclaredField("mDisplayInfo")
            mDisplayInfo_Field.isAccessible = true
            val mDisplayInfo = mDisplayInfo_Field.get(display)

            val cls_displayInfo = mDisplayInfo::class.java

            val logicalWidth_Field = cls_displayInfo.getField("logicalWidth")
            logicalWidth_Field.isAccessible = true
            deviceScreenWidth = logicalWidth_Field.getInt(mDisplayInfo)

            val logicalHeight_Field = cls_displayInfo.getField("logicalHeight")
            logicalHeight_Field.isAccessible = true
            deviceScreenHeight = logicalHeight_Field.getInt(mDisplayInfo)
        }catch (e:Exception){
            e.printStackTrace()
            val dm = DisplayMetrics()
            display.getRealMetrics(dm)
            deviceScreenWidth = dm.widthPixels
            deviceScreenHeight = dm.heightPixels
        }
        return Pair(deviceScreenWidth,deviceScreenHeight)
    }
    fun getDeviceWindowWidthHeight(context: Context):Pair<Float,Float>{
        val density = context.resources.displayMetrics.density
        val appWindowWidth1 = context.resources.configuration.screenWidthDp
        val appWindowHeight1 = context.resources.configuration.screenHeightDp
        val windowWidth = appWindowWidth1 * density
        val windowHeight = appWindowHeight1 * density
        return Pair(windowWidth,windowHeight)
    }

    /**
     * 获取当前屏幕方向
     */
    fun getDisplayRotation(context: Activity): Int {
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display
        } else {
            context.windowManager.defaultDisplay
        }
        var screenRotation = Surface.ROTATION_0
        display?.let {
            screenRotation = display.rotation
        }
        return screenRotation
    }
    /**
     * 获取当前屏幕方向
     */
    fun getRotationToDegree(rotation: Int) : Int {
        var screenRotation = 0
        when(rotation) {
            Surface.ROTATION_0 -> {
                screenRotation = 0
            }
            Surface.ROTATION_90 -> {
                screenRotation = 90
            }
            Surface.ROTATION_180 -> {
                screenRotation = 180
            }
            Surface.ROTATION_270 -> {
                screenRotation = 270
            }
        }
        return screenRotation
    }


    fun getScreenOrientation(context: Activity): Int {
        var orientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display
        } else {
            context.windowManager.defaultDisplay
        }
        display?.let {
            val rotation: Int = display.getRotation()
            val dm = DisplayMetrics()
            display.getMetrics(dm)
            val width = dm.widthPixels
            val height = dm.heightPixels
            // 默认方向为横屏 或 竖屏，需要区分
            val rotation_0_180 = (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
            val rotation_90_270 = (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            orientation = if ((rotation_0_180 && height > width) || (rotation_90_270 && width > height)) {
                when (rotation) {
                    Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    else -> {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                }
            } else {
                when (rotation) {
                    Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    else -> {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                }
            }
        }
        return orientation
    }

}