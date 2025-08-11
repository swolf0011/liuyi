package com.android.jide.lib.utils

import android.os.SystemClock
import android.view.View

/**
 * 针对单个控件的防重点击
 * 外部使用[setOnFastClickListener]来调用
 */
internal abstract class NvOnFastClickListener : View.OnClickListener {

    private val minClickDelay = 1000L

    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val curClickTime = SystemClock.elapsedRealtime()
        if (curClickTime - lastClickTime <= minClickDelay) {
            return
        }
        lastClickTime = curClickTime
        onClickNoFast(v)
    }

    abstract fun onClickNoFast(v: View)

}