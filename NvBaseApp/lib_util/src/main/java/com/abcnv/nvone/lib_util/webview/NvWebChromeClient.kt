package com.abcnv.nvone.lib_util.webview

import android.net.Uri
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class NvWebChromeClient: WebChromeClient() {

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
    }

    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        result.confirm()
        return true
    }

    override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
        result.confirm()
        return true
    }


    override fun onJsPrompt(
        view: WebView,
        origin: String,
        message: String,
        defaultValue: String,
        result: JsPromptResult
    ): Boolean {
        result.confirm()
        return true
    }



    override fun onShowFileChooser(
        webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        return true
    }
    override fun onReceivedTitle(webView: WebView, title: String) {
        super.onReceivedTitle(webView, title)
    }


}