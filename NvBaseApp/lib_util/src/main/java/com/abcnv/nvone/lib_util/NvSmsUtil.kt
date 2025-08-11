package com.abcnv.nvone.lib_util

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.telephony.SmsManager

object NvSmsUtil {
    val SENT_SMS_ACTION = "SENT_SMS_ACTION"
    val DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"

    /**
     * 发送短信
     */
    private fun sendSMS(
        context: Context, phoneNumber: String, message: String, sentIntent: Intent,
        deliverIntent: Intent
    ) {
        val sms = SmsManager.getDefault()

        if (sentIntent != null) {
            sentIntent.action = SENT_SMS_ACTION
        }
        val sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (deliverIntent != null) {
            deliverIntent.action = DELIVERED_SMS_ACTION
        }
        val deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0)
        val msgs = sms.divideMessage(message)// 拆分短信
        for (msg in msgs) {
            sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI)
        }
    }



    /**
     * 群发送短信
     */
    fun sends(
        context: Context, phoneNumbers: Array<String>, message: String, sentIntent: Intent,
        deliverIntent: Intent
    ) {
        for (phoneNumber in phoneNumbers) {
            sendSMS(context, phoneNumber, message, sentIntent, deliverIntent)
        }
    }



    /**
     * 得到最后一条短信的会话id
     */
    @SuppressLint("Range")
    fun getLastSmsThreadID(context: Context): Int {
        var threadId = 0
        val cursor = context.contentResolver.query(
            Uri.parse("content://sms/"),
            arrayOf("* ,max(date) from sms--"), null, null, ""
        )
        if (null != cursor && cursor.moveToFirst()) {
            threadId = cursor.getLong(cursor.getColumnIndex("thread_id")).toInt()
        }
        cursor?.close()
        return threadId
    }

    /**
     * 查询全部短信
     */
    fun queryAllSMS(context: Context): Cursor? {
        val uri = Uri.parse("content://sms/")
        return context.contentResolver.query(uri, null, null, null, "date desc")
    }

    /**
     * 查询短信
     */
    fun querySMS(context: Context, selection: String, selectionArgs: Array<String>, order: String): Cursor? {
        val uri = Uri.parse("content://sms/")
        return context.contentResolver.query(uri, null, selection, selectionArgs, order)
    }

    /**
     * 插入发送中的短信,返回ID
     */
    fun insertSMS(context: Context, threadId: Long, body: String, address: String): String? {
        val cv = ContentValues()
        cv.put("thread_id", threadId)
        cv.put("date", System.currentTimeMillis())
        cv.put("body", body)
        cv.put("type", 2)
        cv.put("address", address)
        cv.put("seen", 1)
        val uri = Uri.parse("content://sms/")
        val result = context.contentResolver.insert(uri, cv)
        return result!!.lastPathSegment
    }

    /**
     * 更新短信状态
     */
    fun updateSMSType(context: Context, smsId: Long, type: Int): Int {
        val uri = Uri.parse("content://sms/")
        val cv = ContentValues()
        cv.put("type", type)
        val selectSql = "_id = $smsId"
        return context.contentResolver.update(uri, cv, selectSql, null)
    }

    /**
     * 直接删除短信
     */
    fun deleteSMS(context: Context, smsId: Long) {
        val uri = Uri.parse("content://sms/")
        val selectSql = "_id = $smsId"
        context.contentResolver.delete(uri, selectSql, null)
    }

}