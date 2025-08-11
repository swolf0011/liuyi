package com.abcnv.nvone.lib_framework

import android.app.Activity
import android.content.Intent
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

object NvFileTypeUtil {
    fun openFile(activity: Activity, file: File) {
        try {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // 设置intent的Action属性
            intent.action = Intent.ACTION_VIEW
            // 获取文件file的MIME类型
            val type = getFileMimeTypeOfFileExt(file)
            // 设置intent的data和Type属性。
            intent.setDataAndType( /* uri */Uri.fromFile(file), type)
            // 跳转
            activity.startActivity(intent) // 这里最好try一下，有可能会报错。
            // //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    fun getFileMimeTypeOfFileExt(file: File): String {
        var type = "*/*"
        val fName = file.name
        // 获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fName.lastIndexOf(".")
        if (dotIndex < 0) {
            return type
        }
        /* 获取文件的后缀名 */
        val end = fName.substring(dotIndex, fName.length).lowercase()
        if (end === "") return type
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (i in MIME_MapTable.indices) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end == MIME_MapTable[i][0]) type = MIME_MapTable[i][1]
        }
        return type
    }

    private val MIME_MapTable = arrayOf(
        arrayOf(".class",   "application/octet-stream"),
        arrayOf(".doc",     "application/msword"),
        arrayOf(".docx",    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        arrayOf(".xls",     "application/vnd.ms-excel"),
        arrayOf(".xlsx",    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        arrayOf(".exe",     "application/octet-stream"),
        arrayOf(".apk",     "application/vnd.android.package-archive"),
        arrayOf(".bin",     "application/octet-stream"),
        arrayOf(".gtar",    "application/x-gtar"),
        arrayOf(".gz",      "application/x-gzip"),
        arrayOf(".jar",     "application/java-archive"),
        arrayOf(".mpc",     "application/vnd.mpohun.certificate"),
        arrayOf(".js",      "application/x-javascript"),
        arrayOf(".msg",     "application/vnd.ms-outlook"),
        arrayOf(".pps",     "application/vnd.ms-powerpoint"),
        arrayOf(".ppt",     "application/vnd.ms-powerpoint"),
        arrayOf(".pptx",    "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        arrayOf(".tar",     "application/x-tar"),
        arrayOf(".tgz",     "application/x-compressed"),
        arrayOf(".wps",     "application/vnd.ms-works"),
        arrayOf(".z",       "application/x-compress"),
        arrayOf(".zip",     "application/x-zip-compressed"),
        arrayOf(".pdf",     "application/pdf"),
        arrayOf(".rtf",     "application/rtf"),
        arrayOf(".m3u",     "audio/x-mpegurl"),
        arrayOf(".m4a",     "audio/mp4a-latm"),
        arrayOf(".m4b",     "audio/mp4a-latm"),
        arrayOf(".m4p",     "audio/mp4a-latm"),
        arrayOf(".mpga",    "audio/mpeg"),
        arrayOf(".ogg",     "audio/ogg"),
        arrayOf(".mp2",     "audio/x-mpeg"),
        arrayOf(".mp3",     "audio/x-mpeg"),
        arrayOf(".rmvb",    "audio/x-pn-realaudio"),
        arrayOf(".wav",     "audio/x-wav"),
        arrayOf(".wma",     "audio/x-ms-wma"),
        arrayOf(".wmv",     "audio/x-ms-wmv"),
        arrayOf(".m4u",     "video/vnd.mpegurl"),
        arrayOf(".m4v",     "video/x-m4v"),
        arrayOf(".mov",     "video/quicktime"),
        arrayOf(".mp4",     "video/mp4"),
        arrayOf(".3gp",     "video/3gpp"),
        arrayOf(".asf",     "video/x-ms-asf"),
        arrayOf(".avi",     "video/x-msvideo"),
        arrayOf(".mpe",     "video/mpeg"),
        arrayOf(".mpeg",    "video/mpeg"),
        arrayOf(".mpg",     "video/mpeg"),
        arrayOf(".mpg4",    "video/mp4"),
        arrayOf(".bmp",     "image/bmp"),
        arrayOf(".jpeg",    "image/jpeg"),
        arrayOf(".jpg",     "image/jpeg"),
        arrayOf(".gif",     "image/gif"),
        arrayOf(".png",     "image/png"),
        arrayOf(".c",       "text/plain"),
        arrayOf(".conf",    "text/plain"),
        arrayOf(".cpp",     "text/plain"),
        arrayOf(".h",       "text/plain"),
        arrayOf(".htm",     "text/html"),
        arrayOf(".html",    "text/html"),
        arrayOf(".java",    "text/plain"),
        arrayOf(".log",     "text/plain"),
        arrayOf(".prop",    "text/plain"),
        arrayOf(".rc",      "text/plain"),
        arrayOf(".sh",      "text/plain"),
        arrayOf(".txt",     "text/plain"),
        arrayOf(".xml",     "text/plain"),

        arrayOf("",         "*/*")
    )


    fun getImageMimeType(filename: String): String {
        try {
            val mimeType = readImageType(filename)
            return String.format("image/%s", mimeType)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 读取文件类型
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private fun readImageType(filename: String): String {
        var fis: FileInputStream? = null
        try {
            val f = File(filename)
            if (!f.exists() || f.isDirectory || f.length() < 8) {
                return ""
            }
            fis = FileInputStream(f)
            val bufHeaders = readInputStreamAt(fis, 0, 8)
            if (isJPEGHeader(bufHeaders)) {
                val skiplength = f.length() - 2 - 8 //第一次读取时已经读了8个byte,因此需要减掉
                val bufFooters = readInputStreamAt(fis, skiplength, 2)
                if (isJPEGFooter(bufFooters)) {
                    return "jpeg"
                }
            }
            if (isPNG(bufHeaders)) {
                return "png"
            }
            if (isGIF(bufHeaders)) {
                return "gif"
            }
            if (isWEBP(bufHeaders)) {
                return "webp"
            }
            if (isBMP(bufHeaders)) {
                return "bmp"
            }
            if (isICON(bufHeaders)) {
                return "ico"
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                fis?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return ""
    }

    /**
     * 标示一致性比较
     *
     * @param buf     待检测标示
     * @param markBuf 标识符字节数组
     * @return 返回false标示标示不匹配
     */
    private fun compare(buf: ByteArray, markBuf: ByteArray): Boolean {
        for (i in markBuf.indices) {
            val b = markBuf[i]
            val a = buf[i]
            if (a != b) {
                return false
            }
        }
        return true
    }

    /**
     * @param fis        输入流对象
     * @param skiplength 跳过位置长度
     * @param length     要读取的长度
     * @return 字节数组
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun readInputStreamAt(fis: FileInputStream, skiplength: Long, length: Int): ByteArray {
        val buf = ByteArray(length)
        fis.skip(skiplength)
        val read = fis.read(buf, 0, length)
        return buf
    }

    private fun isBMP(buf: ByteArray): Boolean {
        val markBuf = "BM".toByteArray() //BMP图片文件的前两个字节
        return compare(buf, markBuf)
    }

    private fun isICON(buf: ByteArray): Boolean {
        val markBuf = byteArrayOf(0, 0, 1, 0, 1, 0, 32, 32)
        return compare(buf, markBuf)
    }

    private fun isWEBP(buf: ByteArray): Boolean {
        val markBuf = "RIFF".toByteArray() //WebP图片识别符
        return compare(buf, markBuf)
    }

    private fun isGIF(buf: ByteArray): Boolean {
        var markBuf = "GIF89a".toByteArray() //GIF识别符
        if (compare(buf, markBuf)) {
            return true
        }
        markBuf = "GIF87a".toByteArray() //GIF识别符
        return if (compare(buf, markBuf)) {
            true
        } else false
    }

    private fun isPNG(buf: ByteArray): Boolean {
        val markBuf = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A) //PNG识别符
        // new String(buf).indexOf("PNG")>0 //也可以使用这种方式
        return compare(buf, markBuf)
    }

    /**
     * JPEG开始符
     *
     * @param buf
     * @return
     */
    private fun isJPEGHeader(buf: ByteArray): Boolean {
        val markBuf = byteArrayOf(0xff.toByte(), 0xd8.toByte())
        return compare(buf, markBuf)
    }

    /**
     * JPEG结束符
     *
     * @param buf
     * @return
     */
    private fun isJPEGFooter(buf: ByteArray): Boolean {
        val markBuf = byteArrayOf(0xff.toByte(), 0xd9.toByte())
        return compare(buf, markBuf)
    }

}