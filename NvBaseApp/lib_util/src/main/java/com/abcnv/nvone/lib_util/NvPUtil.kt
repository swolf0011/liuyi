package com.abcnv.nvone.lib_util



object NvPUtil {
    fun p(any:Any, code:String = "0011=="){
        if(BuildConfig.DEBUG){
            println("${code}::${any}")
        }
    }
}