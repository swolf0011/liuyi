package com.abcnv.nvone.lib_https.test.data

data class DResp<T>(var code: Int = 0, var msg: String = "", var data: T? = null) {
    fun isSuccess(): Boolean {
        return code == 200
    }
}