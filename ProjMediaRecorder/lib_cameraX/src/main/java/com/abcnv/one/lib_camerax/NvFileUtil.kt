package com.abcnv.one.lib_camerax

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.*
import java.nio.charset.Charset

object NvFileUtil {

    /**
     * /storage/emulated/0
     * @return File
     */
    fun getExternalStorageDirectory(): File {
        return Environment.getExternalStorageDirectory()
    }

    /**
     * /storage/emulated/0
     * @return File
     */
    fun getRootDir(): File {
        return File("/storage/emulated/0")
    }
    /**
     * /storage/emulated/0/${name}
     * @return File
     */
    fun getRootNameDir(name:String): File {
        return File("/storage/emulated/0/${name}")
    }
    /**
     * /system
     * @return File
     */
    fun getSystemDir(): File {
        return Environment.getRootDirectory()
    }

    /**
     * /storage/emulated/0/Android/data/${包名}/files
     *
     * @param context Context
     * @return File
     */
    fun getExternalFilesDir(context: Context): File {
        val dir = context.getExternalFilesDir(null)
        if (dir != null) {
            return dir
        }
        return File("/storage/emulated/0/Android/data/${context.packageName}/files")
    }

    /**
     * /storage/emulated/0/Android/data/${包名}/files/${dir_name}
     *
     * @param context Context
     * @param dirName String
     * @return File
     */
    fun getExternalFilesDir(context: Context, dir_name: String): File {
        val dir = context.getExternalFilesDir(dir_name)
        if (dir != null) {
            return dir
        }
        return File("/storage/emulated/0/Android/data/${context.packageName}/files")
    }

    /**
     * /storage/emulated/0/Android/data/${包名}/files
     *
     * @param context Context
     * @return File
     */
    fun getExternalCacheDir(context: Context): File {
        val dir = context.getExternalCacheDir()
        if (dir != null) {
            return dir
        }
        return File("/storage/emulated/0/Android/data/${context.packageName}/cache")
    }

    /**
     * /data/user/0/${包名} === /data/data/${包名}/
     *
     * @param context Context
     * @return File
     */
    fun getDataDir(context: Context): File {
        return context.getDataDir()
    }

    /**
     * /data/user/0/${包名}/cache  === /data/data/${包名}/cache
     *
     * @param context Context
     * @return File
     */
    fun getDataCacheDir(context: Context): File {
        return context.cacheDir
    }

    /**
     * /data/user/0/${包名}/files === /data/data/${包名}/files
     *
     * @param context Context
     * @return File
     */
    fun getDataFilesDir(context: Context): File {
        return context.getFilesDir()
    }


    /**
     *
     * @param file File
     * @param charset String
     * @return String
     */
    fun readString(file: File, charset: String = "UTF-8"): String {
        val result = StringBuffer("")
        if (file.exists()) {
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = FileInputStream(file)
                if (fileInputStream != null) {
                    val buffer = ByteArray(4 * 1024)
                    var length = -1
                    while (fileInputStream.read(buffer).also { length = it } != -1) {
                        result.append(String(buffer, 0, length, Charset.forName(charset)))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                closeInputStream(fileInputStream)
            }
        }
        return result.toString()
    }

    /**
     *
     * @param inputStream InputStream
     * @param charset String
     * @return String
     */
    fun readString(inputStream: InputStream, charset: String = "UTF-8"): String {
        val result = StringBuffer("")
        try {
            val buffer = ByteArray(4 * 1024)
            var length = -1
            while (inputStream.read(buffer).also { length = it } != -1) {
                result.append(String(buffer, 0, length, Charset.forName(charset)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result.toString()
    }

    private fun closeInputStream(inputStream: InputStream?) {
        try {
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun closeOutputStream(outputStream: OutputStream?) {
        try {
            outputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     * @param file File
     * @param inSampleSize Int 1(1/1 size4),4(1/4 size)
     * @return Bitmap?
     */
    fun readBitmap(file: File, inSampleSize: Int = 1): Bitmap? {
        var bitmap: Bitmap? = null
        if (file.exists()) {
            val options = BitmapFactory.Options()
            if (inSampleSize > 0) {
                options.inSampleSize = inSampleSize
            }
            bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        }
        return bitmap
    }

    /**
     *
     * @param file File
     * @return ByteArray?
     */
    fun readByteArray(file: File): ByteArray? {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(file)
            val buffer = ByteArray(file.length().toInt())
            fileInputStream.read(buffer)
            return buffer
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeInputStream(fileInputStream)
        }
        return null
    }

    /**
     * readByteArray
     */
    /**
     *
     * @param filePath String
     * @param length Int
     * @param start Long
     * @return ByteArray?
     */
    fun readByteArray(filePath: String, length: Int, start: Long): ByteArray? {
        var raf: RandomAccessFile? = null
        try {
            if (start >= 0 && length > 0) {
                val buffer = ByteArray(length)
                raf = RandomAccessFile(filePath, "rw")
                raf.seek(start)
                raf.read(buffer)
                return buffer
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                raf?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /*****************************************************************/


    /**
     *
     * @param saveFile File
     * @param inputStream InputStream
     * @param append Boolean
     * @return Boolean
     */
    fun write(saveFile: File, inputStream: InputStream, append: Boolean = false): Boolean {
        var b = false
        var fileOutputStream: FileOutputStream? = null
        try {
            if (inputStream != null) {
                fileOutputStream = FileOutputStream(saveFile, append)
                val buffer = ByteArray(4 * 1024)
                var length = -1
                while (inputStream.read(buffer).also { length = it } != -1) {
                    fileOutputStream.write(buffer, 0, length)
                }
                fileOutputStream.flush()
                b = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeInputStream(inputStream)
            closeOutputStream(fileOutputStream)
        }
        return b
    }

    /**
     *
     * @param saveFile File
     * @param buffer ByteArray
     * @param append Boolean 是否追加
     * @return Boolean
     */
    fun write(saveFile: File, buffer: ByteArray, append: Boolean = false): Boolean {
        var b = false
        var fileOutputStream: FileOutputStream? = null
        try {
            if (buffer != null) {
                fileOutputStream = FileOutputStream(saveFile, append)
                fileOutputStream.write(buffer)
                fileOutputStream.flush()
                b = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeOutputStream(fileOutputStream)
        }
        return b
    }

    /**
     * 从start位置把buffer内容全部write到filePath中
     *
     * @param saveFile File

     * @param buffer ByteArray
     * @param start Long 插入位置，从这个位置之后的数据就会覆蓋
     * @return Boolean
     */
    fun write(saveFile: File, buffer: ByteArray, start: Long): Boolean {
        var b = false
        var raf: RandomAccessFile? = null
        try {
            if (buffer != null) {
                raf = RandomAccessFile(saveFile, "rw")
                raf.seek(start)
                raf.write(buffer)
                b = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                raf?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return b
    }

    /**
     *
     * @param sourceFile File
     * @param targetFile File
     * @return Boolean
     */
    fun copyFile(sourceFile: File, targetFile: File): Boolean {
        var b = false
        var inBuff: BufferedInputStream? = null
        var outBuff: BufferedOutputStream? = null
        try {
            inBuff = BufferedInputStream(FileInputStream(sourceFile))
            outBuff = BufferedOutputStream(FileOutputStream(targetFile))
            val bs = ByteArray(1024 * 4)
            var length = -1
            while (inBuff.read(bs).also { length = it } != -1) {
                outBuff.write(bs, 0, length)
            }
            outBuff.flush()
            b = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeInputStream(inBuff)
            closeOutputStream(outBuff)
        }
        return b
    }


}