package com.abcnv.nvone.lib_base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * @Description: APP异常保存到本地文件中的处理类
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
*/
class NvExceptionHandler  private constructor(val context: Context) : Thread.UncaughtExceptionHandler {
    var TAG = "0011==${javaClass.simpleName}"

    companion object {
        @Volatile
        private var exceptionHandler: NvExceptionHandler? = null
        private var handler: Thread.UncaughtExceptionHandler? = null

        /**
         * 生成APP异常处理对象
         * @param context Context
         * @return NyExceptionHandler
         */
        @Synchronized
        fun getInstance(context:Context): NvExceptionHandler {
            if (exceptionHandler == null) {
                exceptionHandler = NvExceptionHandler(context)
            }
            return exceptionHandler!!
        }
    }

    init {
        handler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        Log.i(TAG,"=>NYExceptionHandler init")
    }
    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.i(TAG,"=>uncaughtException:出异常了！！${e.cause}")
        dumpExceptionToFile(e)
        uploadExceptionToServer()
        //系统默认的异常处理器来处理
        handler?.uncaughtException(t, e)
    }

    //	上传到Server
    private fun uploadExceptionToServer() {
        //  0011 上传到Server
    }

    //可以根据自己需求来,比如获取手机厂商、型号、系统版本等等
    private fun dumpExceptionToFile(e: Throwable) {
        var userId = "0011"
        val timeMillis = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd_HHmmss").format(Date(timeMillis))
        val logName = "${userId}_${time}_${Build.MODEL}_err.txt"
        val file = File(File("/storage/emulated/0/err/${userId}/"), logName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        Log.i(TAG,"=>ExceptionFilePath=${file.absolutePath}")
        var pw:PrintWriter? = null
        try {
            pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println("--------${time}  开始写异常日志---------------")
            pw.println("应用版本号 : ${getVersionCode(context)}")
            pw.println("Android版本号 : ${Build.VERSION.RELEASE}")
            pw.println("手机型号 : ${Build.MODEL}")
            pw.println("CUP架构 : ${Build.CPU_ABI}")
            //异常日志
            e.printStackTrace(pw)
            pw.println("--------${time}  结束写异常日志---------------")
            pw.flush()
            Log.i(TAG,"=>writer exception file success")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i(TAG,"=>writer exception file fail")
        }finally {
            try{
                pw?.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取自己应用内部的版本号
     */
    fun getVersionCode(context: Context): Int {
        val manager = context.packageManager
        var code = 0
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            code = info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return code
    }

}