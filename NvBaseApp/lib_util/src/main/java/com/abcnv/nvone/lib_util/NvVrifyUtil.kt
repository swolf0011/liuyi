package com.swolf.ly.common.util

import java.util.regex.Pattern


object NvVrifyUtil {
    fun isEmpty(str: String): Boolean {
        return null == str || str.length == 0
    }

    fun isStringOutLength(str: String, length: Int): Boolean {
        if (isEmpty(str)) {
            return true;
        }
        return str.length > length
    }

    fun isEmail(email: String): Boolean {
        var str =
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        var p = Pattern.compile(str)
        var m = p.matcher(email)
        return m.matches()
    }

    fun isPhoneNumber(phoneNumber: String): Boolean {
        if (isEmpty(phoneNumber)) {
            return false
        }
        if (phoneNumber.length != 11) {
            return false
        }
        val s = phoneNumber.substring(0, 1)
        return "1" != s
    }

    fun isNumeric(str: String): Boolean {
        var p = Pattern.compile("[0-9]*")
        var m = p.matcher(str)
        return m.matches()
    }
    fun checkIdCard(idCard: String): Boolean {
        val regex = "[1-9]\\d{13,16}[X0-9]{1}"
        var p = Pattern.compile(regex)
        var m = p.matcher(idCard)
        return m.matches()
    }
    fun checkDigit(digit: String): Boolean {
        val regex = "\\\\-?[1-9]\\\\d+"
        var p = Pattern.compile(regex)
        var m = p.matcher(digit)
        return m.matches()
    }

    fun checkDecimals(decimals: String): Boolean {
        val regex = "\\-?[1-9]\\d+(\\.\\d+)?"
        var p = Pattern.compile(regex)
        var m = p.matcher(decimals)
        return m.matches()
    }
    fun checkChinese(chinese: String): Boolean {
        val regex = "^[\u4E00-\u9FA5]+$";
        var p = Pattern.compile(regex)
        var m = p.matcher(chinese)
        return m.matches()
    }
}