package com.abc_swolf.ly.app02_base_router

import android.app.Application
import com.abc_swolf.ly.lib_arouter.ARouterUtil

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/3/24 22:55
 */
class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()
        ARouterUtil.init(this)
    }
}