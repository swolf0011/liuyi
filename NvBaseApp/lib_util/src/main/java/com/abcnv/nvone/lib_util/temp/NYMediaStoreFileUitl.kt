package com.abcly.swolf.lib_util.file

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import java.io.File

object NYMediaStoreFileUitl {
    fun getMediaStoreFile(context: Context, type: Int, ext: String = ""): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        val columns = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME
        )
        val orderBy: String? = when (type) {
            1 -> MediaStore.Files.FileColumns.DATE_ADDED
            2 -> MediaStore.Files.FileColumns.DATE_MODIFIED
            else -> null
        }

        val resolver: ContentResolver = context.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val select = if (ext.isEmpty()) {
            ""
        } else {
//            "(_data LIKE '%.pdf')"
            "(_data LIKE '%.${ext}')"
        }
        val selectionArgs = null
        val sortOrder = "$orderBy  desc"
        val cursor = resolver.query(uri, columns, select, selectionArgs, sortOrder)
        cursor?.apply {
            val data = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            while (cursor.moveToNext()) {
                val path: String = this.getString(data)
                val column = if (type == 1) {
                    MediaStore.Files.FileColumns.DATE_ADDED
                } else {
                    MediaStore.Files.FileColumns.DATE_MODIFIED
                }
                val modifyTime = cursor.getColumnIndexOrThrow(column)
                if (File(path).exists()) {
                    val map = mutableMapOf<String, Any>()
                    map["path"] = path
                    map["time"] = modifyTime * 1000
                    list.add(map)
                }
            }
            cursor.close()
        }

        return list
    }

}