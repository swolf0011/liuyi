package com.abcnv.nvone.lib_webviewx5

import android.util.Log
import android.webkit.ValueCallback
import com.tencent.smtt.sdk.WebView

object Java2JSInterface {
    fun javaCallJsFun(webView: WebView, funName: String, msg: String, callback: (String) -> Unit) {
        val methondStr = if (msg.length == 0) {
            "javascript:${funName}()"
        } else {
            "javascript:${funName}('${msg}')"
        }
        Log.d("0011==", "javaCallJsFun-----$methondStr")
        webView.evaluateJavascript(methondStr) { result ->
            Log.d("0011==", "javaCallJsFun result-----$result")
            callback.invoke(result ?: "")
        }
    }
}