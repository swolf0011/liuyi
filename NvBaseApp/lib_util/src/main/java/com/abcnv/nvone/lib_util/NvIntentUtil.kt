package com.abcnv.nvone.lib_util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.net.URISyntaxException

object NvIntentUtil {
    /**
     * 调用浏览器
     *
     * @param context
     * @param urlStr
     */
    fun browser(context: Context, urlStr: String?) {
        val uri = Uri.parse(urlStr)
        val it = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(it)
    }

    /**
     * 百度地图上显示该经纬度的当前位置
     *
     * @param ctontext
     * @param latitude  纬度
     * @param longitude 经度
     * @param title
     * @param content
     * @param appName
     */
    fun openBaiduMap(
        ctontext: Context,
        latitude: Double,
        longitude: Double,
        title: String?,
        content: String?,
        appName: String?
    ) {
        val sb = StringBuffer("intent://map/marker?location=")
        sb.append(latitude)
        sb.append(",")
        sb.append(longitude)
        sb.append("&title=")
        sb.append(title)
        sb.append("&content=")
        sb.append(content)
        sb.append("&src=")
        sb.append(appName)
        sb.append("#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end")
        try {
            val intent = Intent.parseUri(sb.toString(), Intent.URI_INTENT_SCHEME)
            if (File("/data/data/" + "com.baidu.BaiduMap").exists()) {
                ctontext.startActivity(intent)
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    /**
     * 高德地图上显示该经纬度的当前位置
     *
     * @param ctontext
     * @param latitude  纬度
     * @param longitude 经度
     * @param appName
     * @param poiname
     */
    fun openGaoDeMap(
        ctontext: Context,
        latitude: Double,
        longitude: Double,
        appName: String?,
        poiname: String?
    ) {
        val sb = StringBuffer("androidamap://viewMap?sourceApplication=")
        sb.append(appName)
        sb.append("&poiname=")
        sb.append(poiname)
        sb.append("&lat=")
        sb.append(latitude)
        sb.append("&lon=")
        sb.append(longitude)
        sb.append("&dev=0")
        try {
            val intent = Intent.parseUri(sb.toString(), Intent.URI_INTENT_SCHEME)
            ctontext.startActivity(intent)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    /**
     * 高德地图上显示该经纬度的当前位置
     *
     * @param ctontext
     * @param latitude  纬度
     * @param longitude 经度
     * @param appName
     */
    fun openGaoDeMap2(ctontext: Context, latitude: Double, longitude: Double, appName: String) {
        val uri =
            Uri.parse("androidamap://navi?sourceApplication=$appName&lat=$latitude&lon=$longitude&dev=0")
        val intent = Intent("android.intent.action.VIEW", uri)
        intent.setPackage("com.autonavi.minimap")
        ctontext.startActivity(intent)
    }

    /**
     * 显示某个坐标在地图上
     *
     * @param context
     * @param latitude  纬度
     * @param longitude 经度
     */
    fun openMap(context: Context, latitude: Float, longitude: Float) {
        val uri = Uri.parse("geo:$latitude,$longitude")
        val it = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(it)
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    fun dial(context: Context, phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val it = Intent(Intent.ACTION_DIAL, uri)
        context.startActivity(it)
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    fun call(context: Context, phoneNumber: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val uri = Uri.parse("tel:$phoneNumber")
            val it = Intent(Intent.ACTION_CALL, uri)
            context.startActivity(it)
        } else {
            Toast.makeText(context, "Manifest.permission.CALL_PHONE 权限不可用！", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * 跳转发送短信或彩信页面
     *
     * @param ctontext
     * @param sms_body 发送的消息
     */
    fun toSendPage(ctontext: Context, sms_body: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.putExtra("sms_body", sms_body)
        intent.type = "vnd.android-dir/mms-sms"
        ctontext.startActivity(intent)
    }

    /**
     * 发送短信
     *
     * @param context
     * @param phoneNumber
     * @param content
     */
    fun sms(context: Context, phoneNumber: String, content: String?) {
        val uri = Uri.parse("smsto:$phoneNumber")
        val it = Intent(Intent.ACTION_SENDTO, uri)
        it.putExtra("sms_body", content)
        context.startActivity(it)
    }

    /**
     * 播放媒体文件
     *
     * @param ctontext
     * @param mp3Path  mp3路径:"/sdcard/cwj.mp3"
     */
    fun playMp3(ctontext: Context, mp3Path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse("file://$mp3Path")
        intent.setDataAndType(uri, "audio/mp3")
        ctontext.startActivity(intent)
    }


    /**
     * 播放媒体文件
     *
     * @param context
     * @param pathSegment "1"
     */
    fun play(context: Context, pathSegment: String?) {
        val uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, pathSegment)
        val it = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(it)
    }
}