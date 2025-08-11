package com.abcnv.nvone.lib_https

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class NvRetrofitManagerUpload {

    private val DEFAULT_CONNECT_SECONDS = 10L
    private val DEFAULT_WRITE_SECONDS = 600L
    private val DEFAULT_READ_SECONDS = 600L
    private lateinit var mFileRetrofit: Retrofit

    fun <T> createServiceApi(service: Class<T>): T {
        return mFileRetrofit.create(service)
    }

    private constructor(
        fileUrl: String,
        customInterceptor: Interceptor? = null,
        networkInterceptor: Interceptor? = null
    ) {
        initRetrofit(fileUrl, customInterceptor, networkInterceptor)
    }

    companion object {
        @Volatile
        private var retrofitManager: NvRetrofitManagerUpload? = null

        @Synchronized
        fun getInstance(
            fileUrl: String,
            customInterceptor: Interceptor? = null,
            networkInterceptor: Interceptor? = null
        ): NvRetrofitManagerUpload {
            if (retrofitManager != null) {
                return retrofitManager!!
            }
            retrofitManager =
                NvRetrofitManagerUpload(fileUrl, customInterceptor, networkInterceptor)
            return retrofitManager!!
        }
    }

    private fun initRetrofit(
        fileUrl: String,
        customInterceptor: Interceptor?,
        networkInterceptor: Interceptor?
    ) {
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(NvDeviceInterceptor())
        customInterceptor?.let {
            builder.addInterceptor(it)
        }
        builder.addNetworkInterceptor(NvLogInterceptorUpload())
        networkInterceptor?.let {
            builder.addNetworkInterceptor(it)
        }
        val mOkHttpClient = builder.build()

        mFileRetrofit = Retrofit.Builder()
            .client(mOkHttpClient) //设置使用okhttp网络请求
            .baseUrl(fileUrl) //设置服务器路径
            //对返回字符串的支持，注意这个要写在gson之前
            .addConverterFactory(ScalarsConverterFactory.create()) //对gson的支持
            .addConverterFactory(GsonConverterFactory.create()) //对rxjava的支持
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync()) //防止rxjava oom，去掉.subscribeOn(Schedulers.io())
            .build()
    }
}