package com.abcnv.nvone.lib_framework

import com.google.gson.Gson

/**
 * @Description:
 *
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
object NvGsonUtil {

    fun toJson(t: Object): String {
        return Gson().toJson(t)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return Gson().fromJson(json, clazz)
    }
}

