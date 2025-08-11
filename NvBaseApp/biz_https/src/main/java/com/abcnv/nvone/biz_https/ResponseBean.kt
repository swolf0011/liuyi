package com.abcnv.nvone.biz_https

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
class ResponseBean<T> {
    var status = 0 //200
    var msg = ""
    var data: T? = null

    companion object{
        val success = 200
        fun isSuccess(status:Int):Boolean{
            return status == success
        }
    }

    override fun toString(): String {
        return "{status:${status},msg:${msg},data:${data}}"
    }
}