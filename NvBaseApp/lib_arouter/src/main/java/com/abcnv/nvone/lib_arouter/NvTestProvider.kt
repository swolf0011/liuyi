package com.abcnv.nvone.lib_arouter

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route


/**
 * @Description:
 *
 * @Use:
 *
 * @property
 *
 * @Author liuyi
 */
@Route(path = "${NvARouterPath.route_provider_test}")
class NvTestProvider : INvProvider {

    override fun getUserName(userId: String): String {
        return "getUserName => ${userId}"
    }

    override fun handlerJson(json: String): Any {
        return "handlerJson => ${json}"
    }

    override fun init(context: Context?) {

    }
}