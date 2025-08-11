package com.abcnv.nvone.lib_util

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.experimental.and

object NvFormatUtil {

    var money_prefix = "￥"
    fun isPhoneNumber(phoneNum: String) : Boolean {
        val compile = Pattern.compile("^(13|14|15|16|17|18|19)\\d{9}$")
        val matcher = compile.matcher(phoneNum)
        return matcher.matches()
    }

    fun getEncryptPhone(phone: String?) : String {
        if (phone == null || phone.isEmpty()) {
            return ""
        }
        if (isPhoneNumber(phone)) {
            return phone.substring(0, 3) + "****" + phone.subSequence(7, 11)
        } else {
            if (phone.length > 6) {
                return phone.substring(0, 3) + "****" + phone.subSequence(
                    phone.length - 3,
                    phone.length
                )
            }
        }

        return phone
    }

    /**
     * 判断是否为不规则手机号
     * @param phone String
     * @return Boolean
     */
    fun isUnNormalPhone(phone: String):Boolean{
        val trueStr = phone.trim()
        if (!isNumber(trueStr) || trueStr.isEmpty() || phone.length < 6 || phone.length > 30){
            return true
        }
        return false
    }

    /**
     * 判断为不规则验证码
     * @param code String
     * @return Boolean
     */
    fun isUnNormalCode(code: String):Boolean {
        val trueStr = code.trim()
        if (!isNumber(trueStr) || trueStr.isEmpty() || trueStr.length != 4){
            return true
        }
        return false
    }

    /**
     * 判断是否为纯数字
     * @param str String
     * @return Boolean
     */
    fun isNumber(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum: Matcher = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * m和km格式距离
     *
     * @param distance
     * @return
     */
    fun formatDistance(distance: Float): String? {
        var distance = distance
        return if (distance >= 1000) {
            if (distance % 1000 == 0f) {
                distance = distance / 1000
                String.format("%.0f", distance) + "km"
            } else {
                distance = distance / 1000
                String.format("%.1f", distance) + "km"
            }
        } else {
            String.format("%.0f", distance) + "m"
        }
    }

    /**
     * 格式钱币 money_prefix+"#,##0.00"
     *
     * @param distance
     * @param money_prefix
     * @return
     */
    fun formatMoney(distance: Double, money_prefix: String): String? {
        return money_prefix + DecimalFormat("#,##0.00").format(distance)
    }

    /**
     * 格式化Double为Money "###,###,###,###.##"
     */
    fun formatMoney(distance: Double): String? {
        return DecimalFormat("###,###,###,###.##").format(distance)
    }

    /**
     * 转换成模糊查询的字符串 %xx%
     */
    fun convertToSQLLikeStr(`val`: String): String? {
        return "%" + `val`.trim { it <= ' ' } + "%"
    }

    /**
     * 二进制byte数组用String串来表示
     */
    @SuppressLint("DefaultLocale")
    fun byte2hex(b: ByteArray): String? {
        var hs = ""
        var stmp = ""
        for (n in b.indices) {
            val x = b[n] and (0XFF).toByte()
            stmp = Integer.toHexString(x.toInt())
            hs = if (stmp.length == 1) {
                hs + "0" + stmp
            } else {
                hs + stmp
            }
        }
        return hs.toUpperCase()
    }

    /**
     * 将String的二进制字符串转成数组
     */
    fun hex2Byte(b: String): ByteArray {
        val data = b.toCharArray()
        val l = data.size
        val out = ByteArray(l shr 1)
        var i = 0
        var j = 0
        while (j < l) {
            var f = Character.digit(data[j++], 16) shl 4
            f = f or Character.digit(data[j++], 16)
            out[i] = (f and 0xff).toByte()
            i++
        }
        return out
    }

    /**
     * 将int型的数据转换成byte数组，四个字节
     */
    fun int2Byte(intValue: Int): ByteArray? {
        val b = ByteArray(4)
        for (i in 0..3) {
            b[i] = (intValue shr 8 * (3 - i) and 0xFF).toByte()
        }
        return b
    }

    /**
     * 将byte数组转换成int型，4个字节的数组
     */
    fun byte2Int(b: ByteArray): Int {
        var intValue = 0
        val tempValue = 0xFF
        for (i in b.indices) {
            val x = b[i] and (tempValue.toByte())
            val y = x.rotateLeft(8 * (3 - i))
            intValue += y
        }
        return intValue
    }

    /**
     * 将int型数字转换成两个字节的数组
     */
    fun short2byte(n: Int): ByteArray? {
        val b = ByteArray(2)
        b[0] = (n shr 8).toByte()
        b[1] = n.toByte()
        return b
    }

    /**
     * 将两个字节的数组转成int型
     */
    fun byte2short(b: ByteArray): Int {
        val x = b[1] and ((0xFF).toByte())
        val y = b[0].toShort().rotateLeft(8) and ((0xFF00).toShort())
        return x + y
    }
}