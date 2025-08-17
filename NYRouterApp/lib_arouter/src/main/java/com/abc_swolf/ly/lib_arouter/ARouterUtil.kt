package com.abc_swolf.ly.lib_arouter

import android.app.Activity
import android.app.Application
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter


object ARouterUtil {
    val isLogin = false

    const val route_home_activity = "/home/route_home_activity"
    const val route_pay_activity = "/pay/route_pay_activity"
    const val route_user_activity = "/user/route_user_activity"


    const val route_data_provider = "/data/MyProviderImpl"
    /**
     * 跳转支付模
     * @param path String
     */
    fun navigation(path: String) {
        ARouter.getInstance().build(path).navigation()
    }
    /**
     * 跳转支付模块
     * @param path String
     * @param name String
     */
    fun navigation(path: String, name: String) {
        ARouter.getInstance().build(path).withString("name", name).navigation()
    }
    /**
     * 跳转用户模块
     * @param name String
     */
    fun navigationUserActivity(name: String) {
        ARouter.getInstance().build(route_user_activity).withString("name", name).navigation()
    }

    /**
     * 跳转支付模块
     * @param activity Activity
     * @param name String
     */
    fun navigationPayActivty(activity: Activity, name: String) {
        ARouter.getInstance().build(route_pay_activity).withString("name", name)
            .navigation(activity, object : NavigationCallback {
                override fun onFound(postcard: Postcard?) {
                    val path = postcard!!.path
                    val bundle = postcard!!.extras
                    println("0011 == ${path}==33333")
                }

                override fun onLost(postcard: Postcard?) {
                    val path = postcard!!.path
                    val bundle = postcard!!.extras
                    println("0011 == ${path}==0000")
                }

                override fun onArrival(postcard: Postcard?) {
                    val path = postcard!!.path
                    val bundle = postcard!!.extras
                    println("0011 == ${path}==11111")
                }

                override fun onInterrupt(postcard: Postcard?) {
                    val path = postcard!!.path
                    val bundle = postcard!!.extras
                    println("0011 == ${path}==111223344")
                    navigationUserActivity("User Login")
                }
            })
    }
    /**
     * 获取IMyProvider
     * @return IMyProvider?
     */
    fun buildDataProvidery(): IMyProvider? {
        val p = ARouter.getInstance().build(route_data_provider).navigation()
        if(p == null){
            return null
        }
        return p as IMyProvider
    }

    /**
     * 获取IMyProvider
     * @return IMyProvider?
     */
    fun navigationDataProvidery(): IMyProvider? {
        val p = ARouter.getInstance().navigation(IMyProvider::class.java)
        if(p == null){
            return null
        }
        return p
    }

    /**
     * 初始化ARouter
     * @param app Application
     */
    fun init(app: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(app)

    }
}