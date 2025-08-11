package com.abcnv.nvone.lib_webviewx5

import android.util.Log
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class NvWebViewX5Client : WebViewClient(){
    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {

        if(url.contains("js-call:")){
            if (url.contains("playLogd")) {
                Log.d("X5WebViewMyself", "Js调用android方法shouldOverrideUrlLoading")
            }
            return true
        }
        webView.loadUrl(url) // 不加载自带浏览器
        return true
    }
}