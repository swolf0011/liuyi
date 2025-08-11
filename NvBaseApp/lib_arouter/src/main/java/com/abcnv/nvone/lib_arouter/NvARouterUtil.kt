package com.abcnv.nvone.lib_arouter

import android.app.Activity
import android.app.Application
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

object NvARouterUtil {

    /**
     * 跳转
     */
    fun navigation(route_activity: String) {
        val postcard = ARouter.getInstance().build(route_activity)
        postcard.navigation()
    }
    /**
     * 跳转
     */
    fun navigation(route_activity: String, callbackSetValue: (Postcard) -> Unit) {
        val postcard = ARouter.getInstance().build(route_activity)
        callbackSetValue.invoke(postcard)
        postcard.navigation()
    }

    /**
     * 跳转
     */
    fun navigation(
        activity: Activity,
        route_activity: String,
        navigationCallback: NavigationCallback,
        callbackSetValue: (Postcard) -> Unit,
    ) {
        //Demo
        val callback1 = object : NavigationCallback {
            override fun onFound(postcard: Postcard?) {
                //查到对应路径
                val path = postcard?.path
                val bundle = postcard?.extras
                println("0011 == ${path}==33333")
            }

            override fun onLost(postcard: Postcard?) {
                //无法查到对应路径
                val path = postcard?.path
                val bundle = postcard?.extras
                println("0011 == ${path}==0000")
            }

            override fun onArrival(postcard: Postcard?) {
                //跳转完成
                val path = postcard?.path
                val bundle = postcard?.extras
                println("0011 == ${path}==11111")
            }

            override fun onInterrupt(postcard: Postcard?) {
                //跳转中断
                val path = postcard?.path
                val bundle = postcard?.extras
                println("0011 == ${path}==111223344")
                //拦截后跳转指定页面
                navigation(NvARouterPath.route_activity_user){

                }
            }
        }
        val postcard = ARouter.getInstance().build(route_activity)
        callbackSetValue.invoke(postcard)
        postcard.navigation(activity, navigationCallback)
    }


    /**
     * 获取IMyProvider
     */
    fun buildProvidery(route_provider: String): INvProvider? {
        val p = ARouter.getInstance().build(route_provider).navigation() ?: return null
        return p as INvProvider
    }

    /**
     * 获取INvProvider
     */
    fun <T:IProvider> navigationProvider(clz: Class<T>): T? {
//        val p = ARouter.getInstance().navigation(TestProvider::class.java)
        val p = ARouter.getInstance().navigation(clz) ?: return null
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