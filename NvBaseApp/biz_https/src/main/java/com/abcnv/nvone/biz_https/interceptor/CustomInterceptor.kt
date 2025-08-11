package com.abcnv.nvone.biz_https.interceptor

import com.abcnv.nvone.biz_https.HttpEngine
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("token", HttpEngine.token)
            .method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}