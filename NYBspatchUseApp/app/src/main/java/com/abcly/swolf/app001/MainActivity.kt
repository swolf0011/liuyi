package com.abcly.swolf.app001

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AppUtils
import com.itfitness.incrementupdatedemo.PatchUtil
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ps = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        KPermissionUtil.checkSelfPermission2requestPermissions(
            this, ps,
            KPermissionUtil.ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE
        )

        findViewById<Button>(R.id.button).setOnClickListener {
            Thread {
                // directory_downloads == "/storage/emulated/0/Android/data/com.abcly.swolf.app001/files/Download"
                val directory_downloads =
                    getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                if (!directory_downloads!!.exists()) {
                    directory_downloads!!.mkdirs()
                }
                println("0011 == " + directory_downloads.absolutePath)
                val oldApkFile = File(directory_downloads, "old.apk")
                val newApkFile = File(directory_downloads, "new1.apk")
                val patchFile = File(directory_downloads, "patch")

                if (!oldApkFile.exists() || !patchFile.exists()) {
                    runOnUiThread {
                        toast("原包文件或差分文件不存在，请将test_apk目录下的文件放入:\n${directory_downloads.absolutePath}")
                    }
                    return@Thread
                }
                PatchUtil.patchAPK(
                    oldApkFile.absolutePath,
                    newApkFile.absolutePath,
                    patchFile.absolutePath
                )
                //安装APK
                if (newApkFile.exists()) {
                    AppUtils.installApp(newApkFile)
                }
            }.start()

        }
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}