package com.abcnv.nvone.lib_util

import android.view.View

/**
 * 防重复点击
 */
fun View.onFastClick(callback: (View) -> Unit) {
    setOnClickListener {
        isEnabled = false
        postDelayed({ isEnabled = true }, 5000)
        callback.invoke(it)
    }
}