package com.abcnv.nvone.lib_webviewx5

import android.webkit.JavascriptInterface

class JS2JavaInterfaceval(val callback: (String) -> Unit) {
    // JS 让 Java 吐司
    @JavascriptInterface
    fun jsCallJavaFunStr(data: String) {
        callback.invoke(data ?: "")
    }
    // JS 让 Java 吐司
    @JavascriptInterface
    fun jsCallJavaFun() {
        callback.invoke("")
    }
}