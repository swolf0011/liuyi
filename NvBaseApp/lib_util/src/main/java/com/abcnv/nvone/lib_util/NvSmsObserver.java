package com.abcnv.nvone.lib_util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

/**
 *   <!-- 允许程序读取短信息 -->
 *     <uses-permission android:name="android.permission.READ_SMS" />
 *     <!-- 允许程序监控一个将收到短信息，记录或处理 -->
 *     <uses-permission android:name="android.permission.RECEIVE_SMS" />
 *     <!-- 允许程序发送SMS短信 -->
 *     <uses-permission android:name="android.permission.SEND_SMS" />
 * 短信观察者
 * Created by LiuYi-15973602714
 */
public class NvSmsObserver extends ContentObserver {

    public static void registerSmsObserver(Context context, NvSmsObserver smsObserver) {
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsObserver);
    }

    public static void unregisterSmsObserver(Context context, NvSmsObserver smsObserver) {
        context.getContentResolver().unregisterContentObserver(smsObserver);
    }

    private Context context;

    @SuppressWarnings("unused")
    private static final String[] SMS_PROJECTION1 = new String[]{"address", "person", "date", "type", "body",};

    public NvSmsObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }

    public void onChange(boolean selfChange) {
        // 读取发件箱中的短信，数据库中是以时间降序来存储短信的，所以，第一条记录就是最新的记录
        // 如果把 content://sms/outbox 改成 content://sms/inbox，则可以读取收件箱中的短信

        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/outbox");
        Cursor cursor = cr.query(uri, null, null, null, null);
        cursor.close();


        // 如果要读取指定号码的未读短信，其中address为号码，对应 123456；read为已读/未读标志，0为未读，1为已读；
        // body为短信内容
        Uri uri1 = Uri.parse("content://sms/inbox");
        Cursor cursor1 = cr.query(uri1, null, "address=? and read=?", new String[]{"123456", "0"}, null);
        cursor1.close();

        handler();
    }

    /**
     * 获取未读短信，短信内容格式是：{"SIGN":"SIGN_PHONE_NUMBER","SRC":"X","IMSI": "X"}
     *
     * @author LiuYi-15973602714 2015-5-5 上午11:35:12
     */
    private void handler() {
        ContentResolver cr = context.getContentResolver();
        // 未读短信read为已读/未读标志，0为未读，1为已读； body为短信内容
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = cr.query(uri, null, "read=?", new String[]{"0"}, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {

            // _id为短信编号；address为手机号码；body为短信内容；time为时间，长整型的
            // int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            // long time = cursor.getLong(cursor.getColumnIndex("date"));
            // String msgTemplate =
            // "{\"SIGN\":\"SIGN_PHONE_NUMBER\",\"SRC\":\"X\",\"IMSI\":\"X\"}";
            if (body != null) {
                try {
                    JSONObject jsonObj = new JSONObject(body);
                    String sign = jsonObj.getString("SIGN");
                    String src = jsonObj.getString("SRC");
                    String imsi = jsonObj.getString("IMSI");
                    if ("SIGN_PHONE_NUMBER".equals(sign) && imsi != null) {
                        // 把src,imsi与phoneNumber保存到服务器上去
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
    }
}
