package com.abcnv.nvone.lib_util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup

object NYDimensionsUtil {


    fun measureView(view: View): Pair<Int, Int> {
        var lp = view.layoutParams
        if (lp == null) {
            lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width)
        val lpHeight = lp.height
        val heightSpec = if (lpHeight > 0) {
            View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY)
        } else {
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        }

        view.measure(widthSpec, heightSpec)
        return Pair<Int, Int>(view.measuredWidth, view.measuredHeight)
    }


    /**
     * @param view View
     * @return Pair<Int, Int>
     */
    fun getLocationInWindow(view: View): Pair<Int, Int> {
        val intArray = IntArray(2)
        view.getLocationInWindow(intArray)
        return Pair<Int, Int>(intArray[0], intArray[1])
    }
    /**
     * 相对父View的位置
     * @param view View
     * @return Pair<Int, Int>
     */
    fun getLocalVisibleRect(view: View): Rect {
        val rect= Rect()
        view.getLocalVisibleRect(rect)
        return rect
    }
    /**
     * 相对屏幕的位置
     * @param view View
     * @return Pair<Int, Int>
     */
    fun getGlobalVisibleRect(view: View): Rect {
        val intArray = IntArray(2)
        val rect= Rect()
        view.getGlobalVisibleRect(rect)
        return rect
    }
    /**
     * 获取屏幕度量
     */
    fun getDisplayMetrics(activity: Activity): DisplayMetrics {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm
    }


    fun getStatusBarHeight(context: Context): Int {
        val STATUSBAR_CLASS_NAME = "com.android.internal.R\$dimen"
        val STATUSBAR_FIELD_HEIGHT = "status_bar_height"

        var sbar = 0
        try {
            val c = Class.forName(STATUSBAR_CLASS_NAME)
            val obj = c.newInstance()
            val field = c.getField(STATUSBAR_FIELD_HEIGHT)
            val x = field[obj].toString().toInt()
            sbar = context.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return sbar
    }
}