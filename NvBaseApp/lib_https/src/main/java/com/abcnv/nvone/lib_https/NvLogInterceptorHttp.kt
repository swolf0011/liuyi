package com.abcnv.nvone.lib_https

import android.util.Log
import com.blankj.utilcode.BuildConfig
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

/**
 * @Description:  日志拦截器
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class NvLogInterceptorHttp : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body

        Log.d("0011==","request-------------------  ${request.method}  ${request.url}")
        Log.d("0011==","header-> :{")
        for (i in 0 until request.headers.size) {
            Log.d("0011==","\t${request.headers.name(i)} : ${request.headers.value(i)}")
        }
        Log.d("0011==","}")
        try {
            val bufferedSink = Buffer()
            requestBody?.writeTo(bufferedSink)
            var charset = Charset.forName("utf-8")
            val contentType = requestBody?.contentType()
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("utf-8"))
            }
            val txt = bufferedSink.readString(charset)
            Log.d("0011==","params :: ${txt}")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("0011==","request params :: ${e.message}")
        }
        Log.d("0011==","")


        val response = chain.proceed(request)
        val responseContent = response.body?.string()?:"responseBody is null"
        Log.d("0011==","response---------------------- ${response.code}")
        Log.d("0011==",responseContent)
        Log.d("0011==","")

        val contentType = request.body?.contentType()
        val newBody = responseContent.toResponseBody(contentType)

        val responseNew = response.newBuilder().body(newBody).build()
        return responseNew
    }
}