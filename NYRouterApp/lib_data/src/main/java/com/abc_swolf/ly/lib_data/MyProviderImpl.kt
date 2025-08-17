package com.abc_swolf.ly.lib_data

import android.content.Context
import com.abc_swolf.ly.lib_arouter.ARouterUtil
import com.abc_swolf.ly.lib_arouter.IMyProvider
import com.alibaba.android.arouter.facade.annotation.Route

/**
 * @Description: 实现IMyProvider接口
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/3/27 12:20
 */
@Route(path = ARouterUtil.route_data_provider)
class MyProviderImpl: IMyProvider {
    override fun getUserName(userId: String): String {
        println("0011 == getUserName ==${userId}")
        return  userId+"1213"
    }

    override fun init(context: Context?) {
        println("0011 == MyProviderImpl init")
    }
}