package com.abcnv.nvone.biz_https

import com.abcnv.nvone.biz_https.interceptor.CustomInterceptor
import com.blankj.utilcode.BuildConfig
import okhttp3.Interceptor

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class RetrofitManagerUtil  {
    private val baseUrl = "http://47.96.102.184:3100/"
    private val fileBaseUrl = "http://47.96.102.184:3100/"
    private val customInterceptor: Interceptor? = CustomInterceptor()
    private val networkInterceptor: Interceptor? = null
    var retrofitManager: NvRetrofitManager

    private constructor() {
        retrofitManager = NvRetrofitManager.getInstance(
            baseUrl,
            fileBaseUrl,
            customInterceptor,
            networkInterceptor,
            BuildConfig.DEBUG
        )
    }


    companion object {
        @Volatile
        private var retrofitManagerUtil: RetrofitManagerUtil? = null

        @Synchronized
        fun getInstance(): RetrofitManagerUtil {
            if (retrofitManagerUtil == null) {
                retrofitManagerUtil = RetrofitManagerUtil()
            }
            return retrofitManagerUtil!!
        }
    }

}