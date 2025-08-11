package com.abcnv.nvone.lib_util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

/**
 *@author aise
 *
 *@date 2021/2/7
 *
 *
 *
 */
object NvFormatTimeUtil {
    /**
     * 将毫秒数转为 分:秒
     * 例如：65000   返回01:05
     */
    fun durationToStringOne(milliSecond: Int): String {
        val timeSecond = milliSecond / 1000  // 将毫秒转化为秒
        return String.format("%02d:%02d", timeSecond / 60, timeSecond % 60)  //已播放时长
    }

    /**
     * Timestamp to String
     * @param Timestamp
     * @return String
     */
    fun timeToStringOne(date: Long): String {
        return SimpleDateFormat("yyyy/MM/dd HH:mm").format(date)
    }

    /**
     * 获取当前时间
     */
    fun getCurrentTime(): String {
        val createTimestamp: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat(" HH:mm:ss ")
        return format.format(createTimestamp.time)
    }

    /**
     * 获取当前时间
     */
    fun getCurrentTime(pattern: String = "yyyy/MM/dd HH:mm:ss"): String {
        val createTimestamp: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat(pattern)
        return format.format(createTimestamp.time)
    }

    /**
     * Timestamp to String
     * @param Timestamp
     * @return String
     */
    fun timeToStringOne(date: Long, pattern: String): String {
        return SimpleDateFormat(pattern).format(date)
    }

    fun dateToTimeStamp(strDate: String?, pattern: String = "yyyy-MM-dd HH:mm:ss"): Long {
        val date = SimpleDateFormat(pattern).parse(strDate)
        return date.time
    }

    /**
     * 获取延后的日期
     * @param startTime String
     * @param delayDay Int
     * @return String
     */
    fun getDelayDayTime(startTime: String, delayDay: Int): String {
        val createTimestamp: Calendar = Calendar.getInstance()
        val startTimeLong = dateToTimeStamp(startTime, "yyyy-MM-dd")
        createTimestamp.time = Date(startTimeLong)
        createTimestamp.add(Calendar.DAY_OF_YEAR, delayDay)
        return SimpleDateFormat("yyyy-MM-dd").format(createTimestamp.time)
    }

    /**
     * 将yyyy-MM-dd 格式的时间转换为 {pattern} 格式的时间
     * @param time String
     * @param pattern String
     */
    fun formatTime(time: String, pattern: String = "yyyy-MM-dd"): String {
        val startTimeLong = dateToTimeStamp(time, "yyyy-MM-dd")
        return SimpleDateFormat(pattern).format(startTimeLong)
    }

    /**
     * 根据时长获取格式化的时长字符串
     * xxx小时xx分xx秒
     * xx分xx秒
     * xx秒
     * @param durationMs 单位：毫秒
     */
    fun getDuration(context: Context, durationMs: Long): String {
        val durationS = durationMs / 1000
        if (durationS >= 3600) {
            val hours = durationS / 3600
            val minutes = (durationS % 3600) / 60
            val seconds = durationS % 60
            return String.format("%d:%d:%d", hours, minutes, seconds)
        } else if (durationS >= 60) {
            val minutes = durationS / 60
            val seconds = durationS % 60
            return String.format("%d:%d", minutes, seconds)
        } else {
            return String.format("%ds", durationS)
        }
    }

    // 毫秒格式化为 HH:mm:ss
    fun formatTimeHms(durationMs: Long): String {
        val durationS = durationMs / 1000
        val hours = durationS / 3600
        val minutes = (durationS % 3600) / 60
        val seconds = durationS % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // 秒格式化为 HH:mm:ss
    fun formatTimeS2Hms(durationS: Long): String {
        val hours = durationS / 3600
        val minutes = (durationS % 3600) / 60
        val seconds = durationS % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // 毫秒格式化为 mm:ss
    fun formatTimeMs2Ms(durationMs: Long): String {
        val durationS = durationMs / 1000
        val minutes = durationS / 60
        val seconds = durationS % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // 秒格式化为 mm:ss
    fun formatTimeS2Ms(durationS: Long): String {
        val minutes = durationS / 60
        val seconds = durationS % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}