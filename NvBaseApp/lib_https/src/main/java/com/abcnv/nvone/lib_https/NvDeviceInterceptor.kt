package com.abcnv.nvone.lib_https

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Description: 设备系统拦截器
 *
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class NvDeviceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header("deviceId", DeviceUtils.getUniqueDeviceId()) // 唯一设备ID
            .header("model", DeviceUtils.getManufacturer())  // 厂商
            .header("deviceModel", DeviceUtils.getModel())   // 型号
            .header("versionCode", "${DeviceUtils.getSDKVersionCode()}")
            .header("appPackageName", AppUtils.getAppPackageName())// versionName
            .header("appVersion", AppUtils.getAppVersionName())// versionName
            .header("appVersionCode", "${AppUtils.getAppVersionCode()}")// versionCode
            .header("Accept", "*/*")

            .method(original.method, original.body)
            .build()


        return chain.proceed(request)

    }
}