package com.abcnv.nvone.lib_webviewx5

import android.content.Context
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

object WebViewX5Util {
    fun initX52Appcation(context: Context) {
        /* 设置允许移动网络下进行内核下载。默认不下载，会导致部分一直用移动网络的用户无法使用x5内核 */
        QbSdk.setDownloadWithoutWifi(true)
        /* SDK内核初始化周期回调，包括 下载、安装、加载 */
        QbSdk.setTbsListener(object : TbsListener {
            /**
             * @param stateCode 110: 表示当前服务器认为该环境下不需要下载
             */
            override fun onDownloadFinish(p0: Int) {
            }

            /**
             * @param stateCode 200、232安装成功
             */
            override fun onInstallFinish(p0: Int) {
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            override fun onDownloadProgress(p0: Int) {
            }
        })
        QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖wifi网络下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * 内核下发请求发起有24小时间隔，卸载重装、调整系统时间24小时后都可重置
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(p0: Boolean) {
            }
        })
    }

    fun initWebView(
        webView: WebView,
        url: String,
        js2JavaInterfaceval: JS2JavaInterfaceval,
        jsObj: String
    ) {
        val settings: WebSettings = webView.getSettings()
        //设置JavaScrip
        settings.javaScriptEnabled = true//如果访问的页面中要与Javascript交互，则必须设置支持Javascript
        settings.javaScriptCanOpenWindowsAutomatically = true//支持通过JS打开新窗口
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        settings.allowFileAccess = true//设置可以访问文件
        settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE//不缓存
        settings.domStorageEnabled = true// 开启 DOM storage API 功能
        settings.databaseEnabled = true  //开启 database storage API 功能
        settings.getUserAgentString()// Fix for CB-1405 Google issue 4641

        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        settings.useWideViewPort = true
//
//        settings.useWideViewPort = true //自适应手机屏幕
//        settings.loadWithOverviewMode = true
//        settings.setSupportMultipleWindows(true)
//        settings.allowContentAccess = true
//        settings.loadsImagesAutomatically = true
//        settings.loadWithOverviewMode = true
//        settings.setAppCacheEnabled(false)
//        settings.blockNetworkImage = true

        webView.setWebChromeClient(WebChromeClient())
        webView.setWebViewClient(X5WebViewClient()) // 注册Js调用Java的方法
        webView.addJavascriptInterface(js2JavaInterfaceval, jsObj)
        webView.loadUrl(url)
    }
}