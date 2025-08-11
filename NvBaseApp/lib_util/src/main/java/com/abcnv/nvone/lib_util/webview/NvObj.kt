package com.abcnv.nvone.lib_util.webview

import android.webkit.JavascriptInterface

class NvObj(val callback:(String)->Unit) {
    @JavascriptInterface
    fun jsCallFun(value: String){
        callback.invoke(value)
    }
}