package com.abcnv.nvone.lib_util

import android.content.res.Resources
import android.os.Build

object NvSysUtil {

//    /**
//     * 系统语言类型
//     */
//    fun getSysLanguageSuffix(): String{
//        return LanguageUtils.getSystemLanguage().language
//    }
    /**
     * 获取系统语言类型
     */
    fun getSysLanguage():String{
        return Resources.getSystem().configuration.locales.get(0).language
    }
}