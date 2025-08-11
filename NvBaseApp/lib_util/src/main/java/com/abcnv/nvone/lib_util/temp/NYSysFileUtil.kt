package com.abcly.swolf.lib_util.file

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.*

object NYSysFileUtil {
    val TYPE_PDF = "application/pdf"
    val TYPE_JPEG = "application/jpeg"
    val TYPE_PNG = "application/png"

    /**
     * 文件类型搜索
     * @param activity Activity
     * @param fileSearchType 搜索的文件类型NYFileSearchUtil.TYPE_PDF
     * @param requestCode 搜索请求码，在onActivityResult获取选择的文件
     * val uri = data.data
     * val path = NYSysFileUtil.uri2Path(context, uri)
     * if(path.isEmpty()) return else ${handler}
     */
    fun search(activity: Activity, fileSearchType: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType(fileSearchType)//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * Uri转path 全平台处理方法
     * @param context Context
     * @param uri Uri
     */
    @SuppressLint("ObsoleteSdkInt")
    fun uri2Path(context: Context, uri: Uri): String {
        val isN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        if (isN) {
            return getFilePathForN(context, uri)
        }
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            return getFilePathForKitKat(context, uri)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path ?: ""
        }
        return ""
    }

    /**
     * android4.4及以上处理方法
     * @param context Context
     * @param uri Uri
     */
    private fun getFilePathForKitKat(context: Context, uri: Uri): String {
        // ExternalStorageProvider
        if ("com.android.externalstorage.documents" == uri.authority) {
            //外部存储文档
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if ("com.android.providers.downloads.documents" == uri.authority) {
            //下载文档
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), id.toLong()
            )
            return getDataColumn(context, contentUri, null, null)
        } else if ("com.android.providers.media.documents" == uri.authority) {
            //媒体文档
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            contentUri?.let {
                return getDataColumn(context, it, selection, selectionArgs)
            }
        }
        return ""
    }

    /**
     * android7.0及以上处理方法
     * @param context Context
     * @param uri Uri
     */
    private fun getFilePathForN(context: Context, uri: Uri): String {
        val resolver = context.contentResolver
        var name = ""
        var cursor: Cursor? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            if ("file".equals(uri.scheme, ignoreCase = true)) {
                val path = uri.path
                if (!path.isNullOrEmpty()) {
                    File(path).exists()
                    val strs = path.split("/")
                    name = strs[strs.size - 1]
                    inputStream = FileInputStream(path)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                val projection = null
                val selection = null
                val selectionArgs = null
                val sortOrder = null
                cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder)
                cursor?.let {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    name = it.getString(index)
                    inputStream = resolver.openInputStream(uri)
                }
            }
            inputStream?.let { input ->
                val file = File(context.filesDir, name)
                outputStream = FileOutputStream(file)
                outputStream?.let { output ->
                    var read = 0
                    val maxBufferSize = 10 * 1024 * 1024
                    val bytesAvailable = input.available()
                    val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    val buffers = ByteArray(bufferSize)
                    while (input.read(buffers).also { read = it } != -1) {
                        output.write(buffers, 0, read)
                    }
                    return file.path
                }
            }
            return ""
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            cursor?.close()
        }
        return ""
    }


    /**
     * 获取此Uri的数据列的值。这对于MediaStore uri和其他基于文件的内容提供程序非常有用。
     * @param context Context
     * @param uri Uri
     * @param selection String ::{where条件：“age = ? and sex = ?”}
     * @param selectionArgs Array<String> ::{arrayOf("10","男")}
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String {
        val resolver = context.contentResolver
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        val sortOrder = null
        try {
            cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return ""
    }
    /**
     * 获取系统相册路径
     * @param context Context
     * @param uri Uri
     */
    fun systemPhotoPath(context: Context, uri: Uri): String {
        val resolver = context.contentResolver
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            val selection = null
            val selectionArgs = null
            val sortOrder = null
            cursor = resolver.query(uri,filePathColumn, selection, selectionArgs, sortOrder)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(filePathColumn[0])
                return cursor.getString(index)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return ""
    }
}