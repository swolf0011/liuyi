package com.abc_swolf.ly.lib_arouter

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @Description: Arouter提供IPovider接口
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/3/27 12:18
 */
interface IMyProvider:IProvider {
    fun getUserName(userId:String):String
}