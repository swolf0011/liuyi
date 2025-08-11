package com.abcnv.nvone.lib_arouter

import com.alibaba.android.arouter.facade.template.IProvider
/**
 * @Description:
 *
 * @Use:
 *
 * @property
 *
 * @Author liuyi
 */
interface INvProvider: IProvider {
    fun getUserName(userId:String):String
    fun handlerJson(json: String): Any
}
