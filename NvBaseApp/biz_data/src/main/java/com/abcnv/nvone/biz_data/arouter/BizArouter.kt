package com.abcnv.nvone.biz_data.arouter

import android.app.Activity
import com.abcnv.nvone.lib_arouter.INvProvider
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter

object BizArouter {

    const val route_home_activity = "/home/route_home_activity"
    const val route_pay_activity = "/pay/route_pay_activity"
    const val route_user_activity = "/user/route_user_activity"
    const val route_data_provider = "/data/NvProviderImpl"

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
     * 获取INvProvider
     * @return INvProvider?
     */
    fun buildDataProvidery(): INvProvider? {
        val p = ARouter.getInstance().build(route_data_provider).navigation()
        if (p == null) {
            return null
        }
        return p as INvProvider
    }

}