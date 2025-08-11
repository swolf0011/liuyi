package com.abcnv.nvone.lib_base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
object NvScopeEnv {
    fun globalScopeIO(ioRunFun:()->Unit){
        GlobalScope.launch(Dispatchers.IO) {
            ioRunFun.invoke()
        }
    }
    fun globalScopeMain(ioRunFun:()->Unit){
        GlobalScope.launch(Dispatchers.Main) {
            ioRunFun.invoke()
        }
    }
}