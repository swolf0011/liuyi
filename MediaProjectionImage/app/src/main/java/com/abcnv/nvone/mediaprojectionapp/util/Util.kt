package com.abcnv.nvone.mediaprojectionapp.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

object Util {
    /**
     * 屏幕宽度_屏幕高度_像素密度
     */
    fun getScreenWidthHeightDpi(activity: AppCompatActivity): Triple<Int, Int, Int> {
        val densityDpi = activity.resources.displayMetrics.densityDpi
        val widthPixels = activity.resources.displayMetrics.widthPixels
        val heightPixels = activity.resources.displayMetrics.heightPixels
        return Triple<Int, Int, Int>(widthPixels, heightPixels, densityDpi)
    }

    /**
     * 模拟HOME键返回桌面的功能
     */
    fun simulateHome(activity: AppCompatActivity) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        activity.startActivity(intent)
    }



}