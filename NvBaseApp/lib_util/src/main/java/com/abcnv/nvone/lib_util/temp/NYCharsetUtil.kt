package com.abcly.swolf.lib_util

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

object NYCharsetUtil {
    enum class ECharset(var value: String) {
        US_ASCII("US-ASCII"), ISO_8859_1("ISO-8859-1"), UTF_8("UTF-8"), GBK("GBK");
    }


    fun changeCharset(str: String?, charset: ECharset): String? {
        try {
            if (str != null) {
                val bs = str.toByteArray()
                return String(bs, Charset.forName(charset.value))
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    fun changeCharset(str: String?, oldCharset: ECharset, newCharset: ECharset): String? {
        try {
            if (str != null) {
                val bs = str.toByteArray(charset(oldCharset.value))
                return String(bs, Charset.forName(newCharset.value))
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }
}