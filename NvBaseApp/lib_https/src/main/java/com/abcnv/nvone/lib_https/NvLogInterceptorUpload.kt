package com.abcnv.nvone.lib_https

import android.util.Log
import okhttp3.*
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
class NvLogInterceptorUpload : Interceptor {
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

        return chain.proceed(request)
    }
}