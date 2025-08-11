package com.abcnv.nvone.lib_base.data

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
object NvKVUtil {

    fun initMMKV(appContext: Context) {
        val rootDir = MMKV.initialize(appContext)
    }

    fun getMMKV(): MMKV {
        return MMKV.defaultMMKV()
    }
}