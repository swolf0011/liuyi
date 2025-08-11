package com.abcnv.nvone.lib_base.data

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
object NvKVbiz {

    private val KEY_TEST = "KEY_TEST"

    fun encodeTest(key: String, pwd: String): Boolean {
        return NvKVUtil.getMMKV().encode(key, pwd)
    }

    fun decodeTest(key: String): String {
        return NvKVUtil.getMMKV().decodeString(key, "").toString()
    }


}