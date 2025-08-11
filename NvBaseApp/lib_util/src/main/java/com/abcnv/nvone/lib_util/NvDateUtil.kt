package com.abcnv.nvone.lib_util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object NvDateUtil {
    object DateFormatStr {
        var dateFormat_01 = "yyyy-MM-dd"
        var dateFormat_02 = "yyyy/MM/dd"
        var dateFormat_11 = "yyyy-MM-dd HH:mm:ss"
        var dateFormat_12 = "yyyy/MM/dd HH:mm:ss"
        var dateFormat_21 = "yyyy-MM-dd HH:mm:ss.sss"
        var dateFormat_22 = "yyyy/MM/dd HH:mm:ss.sss"
        var dateFormat_31 = "HH:mm:ss"
        var dateFormat_32 = "HH:mm:ss.sss"
    }

    /**
     *
     * @param date
     * @param dateFormat    日期格式DateFormatStr
     * @return
     */
    fun date2Str(date: Date?, dateFormat: String?): String? {
        return SimpleDateFormat(dateFormat).format(date)
    }


    /**
     *
     * @param str
     * @param dateFormat    日期格式
     * @return
     */
    fun str2Date(str: String?, dateFormat: String?): Date? {
        try {
            return SimpleDateFormat(dateFormat).parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 差毫秒数
     *
     * @param begin
     * @param end
     * @return
     */
    fun compare(begin: Date, end: Date): Long {
        return end.time - begin.time
    }

    /**
     * 相差天数
     *
     * @param begin
     * @param end
     * @return
     */
    fun compareDay(begin: Date, end: Date): Long {
        val lon = end.time - begin.time
        return if (lon % (1000 * 60 * 60 * 24) == 0L) lon / (1000 * 60 * 60 * 24) else lon % (1000 * 60 * 60 * 24) + 1
    }

}