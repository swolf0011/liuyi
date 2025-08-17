package com.abcly.swolf.nymediacodec

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.SurfaceView
import android.widget.Button
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYFileUtil
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYH264Decoder
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYPermissionUtil
import java.io.*

class H264DecoderActivity : AppCompatActivity() {
    private val PERMISSIONS_CODE = 10001
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE"
    )
    private lateinit var surfaceView: SurfaceView
    private lateinit var mStartBtn: Button
    private var mWorking = false
    private val INIT_MANAGER_MSG = 0x01
    private val INIT_MANAGER_DELAY = 500L
    private var mp4_play_path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h264_decoder)
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        mStartBtn.setOnClickListener {
            if (mWorking) {
                NYH264Decoder.getInstance().close()
                mWorking = false
                mStartBtn.text = "start"
            } else {
                mHandler.sendEmptyMessageDelayed(
                    INIT_MANAGER_MSG,
                    INIT_MANAGER_DELAY
                )
                mWorking = true
                mStartBtn.text = "stop"
            }
        }
    }

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == INIT_MANAGER_MSG) {
                NYH264Decoder.getInstance()
                    .startMP4Decode(mp4_play_path, surfaceView.holder.surface)
            }
        }
    }

    private fun initData() {
        val file = NYFileUtil.getExternalFilesDir(this)
        mp4_play_path = file.absolutePath + "/TestInputV.mp4"

    }

    private fun copyResourceToMemory(srcPath: Int, destPath: String) {
        var fileInputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            val file = File(destPath)
            if (file.exists()) {
                return
            }
            file.createNewFile()
            fileInputStream = resources.openRawResource(srcPath)
            fileOutputStream = FileOutputStream(file)
            val bytes = ByteArray(1024)
            while (fileInputStream.read(bytes) > 0) {
                fileOutputStream.write(bytes)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream?.close()
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun initView() {
        surfaceView = findViewById(R.id.surfaceview)
        mStartBtn = findViewById(R.id.btnStartPlay)
    }

    override fun onResume() {
        super.onResume()
        initPermissions()
    }

    private fun initPermissions() {
        val result = NYPermissionUtil.checkPermission(this, PERMISSIONS_STORAGE)
        if (result) {
            copyResourceToMemory(R.raw.video, mp4_play_path)
        } else {
            NYPermissionUtil.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSIONS_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        when (requestCode) {
            PERMISSIONS_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //这里已经获取到了摄像头的权限
                copyResourceToMemory(R.raw.video, mp4_play_path)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("警告！")
                    .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
                    .setPositiveButton("确定") { dialog, which -> // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish()
                    }.show()
            }
            else -> {}
        }
    }
}