package com.abcnv.nvone.biz_data.arouter

import android.content.Context
import com.abcnv.nvone.lib_arouter.INvProvider
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = BizArouter.route_data_provider)
class AppArouterProviderImpl : INvProvider {
    override fun getUserName(userId: String): String {
        println("0011 == getUserName ==${userId}")
        return userId + "1213"
    }

    override fun handlerJson(json: String): Any {
        println("0011 == json ==${json}")
        return Object()
    }

    override fun init(context: Context?) {
        println("0011 == MyProviderImpl init")
    }
}