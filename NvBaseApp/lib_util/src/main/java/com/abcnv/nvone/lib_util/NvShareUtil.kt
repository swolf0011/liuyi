package com.abcnv.nvone.lib_util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File

class NvShareUtil {


    /**
     * 根据文件类型进行分享
     * @param activity 当前activity
     * @param file 导出后的文件
     * @param shareType 分享的文件类型
     * @param authority "com.jideos.jnotes.fileProvider"
     * @param shareTo 分享到的文本
     */
    @RequiresApi(33)
    @SuppressLint("ObsoleteSdkInt")
    fun shareFile(
        activity: Activity,
        file: File,
        shareType: String,
        authority: String,
        shareTo: String = ""
    ) {
        try {
            val exportUri = FileProvider.getUriForFile(activity, authority, file)
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, exportUri)
            shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            shareIntent.type = shareType
            val createChooserIntent = Intent.createChooser(shareIntent, shareTo)
            val targets = mutableListOf<ComponentName>()
//                List<ResolveInfo> queryIntentActivities(@NonNull Intent intent,
//                @NonNull ResolveInfoFlags flags)
            val ps = activity.packageManager.queryIntentActivities(shareIntent, PackageManager.ResolveInfoFlags.of(0L))
            ps.forEach {
                val pn = it.activityInfo.packageName
                if (pn.toLowerCase().contains("com.orion.notein")) {
                    targets.add(ComponentName(pn, it.activityInfo.name))
                }
            }
            val name = Intent.EXTRA_EXCLUDE_COMPONENTS
            createChooserIntent.putExtra(name, targets.toTypedArray())
            activity.startActivity(createChooserIntent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 根据文件类型进行分享
     * @param activity 当前activity
     * @param fileList 导出后的文件s
     * @param shareType 分享的文件类型
     * @param authority "com.jideos.jnotes.fileProvider"
     * @param shareTo 分享到的文本
     */
    @SuppressLint("ObsoleteSdkInt")
    fun shareFiles(
        activity: Activity,
        fileList: List<File>,
        shareType: String,
        authority: String,
        shareTo: String = ""
    ) {
        val shareIntent = Intent()
        val uriList: ArrayList<Uri> = arrayListOf()
        fileList.forEach {
            val exportUri =  FileProvider.getUriForFile(activity, authority, it)
            uriList.add(exportUri)
        }
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        shareIntent.type = shareType
        activity.startActivity(Intent.createChooser(shareIntent, shareTo))
    }
}