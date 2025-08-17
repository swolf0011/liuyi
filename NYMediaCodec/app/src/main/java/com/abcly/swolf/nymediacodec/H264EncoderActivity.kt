package com.abcly.swolf.nymediacodec

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.Camera
import android.media.MediaCodecList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYFileUtil
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYH264Encoder
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYPermissionUtil
import java.io.IOException

class H264EncoderActivity : AppCompatActivity() {
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var surfaceview: SurfaceView
    private lateinit var mBtnPlay: Button
    private var mWorking = false
    private val width = 640
    private val height = 480
    private val framerate = 24
    private val biterate = 8500 * 1000

    private val PERMISSIONS_CODE = 10001
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.CAMERA",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE"
    )

    private var h264Encoder: NYH264Encoder? = null
    private var camera: Camera? = null
    private var parameters: Camera.Parameters? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h264_encoder)
        initView()
        initData()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        initPermissions()
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
                init()
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
    private fun initPermissions() {
        val result = NYPermissionUtil.checkPermission(this, PERMISSIONS_STORAGE)
        if (result) {
            init()
        } else {
            NYPermissionUtil.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSIONS_CODE)
        }
    }

    private fun init() {
        surfaceHolder = surfaceview.holder
        surfaceHolder.addCallback(object:SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                stopWork()
            }
        })
    }


    private fun initData() {
        mWorking = false
        supportAvcCodec()
    }

    private fun initListener() {
        val file = NYFileUtil.getExternalFilesDir(this)
        val path = file.absolutePath + "/TestOutput.h264"
        mBtnPlay.setOnClickListener {
            if (mWorking) {
                stopWork()
                mWorking = false
                mBtnPlay.text = "start"
                showSaveFilePath(path)
            } else {
                startWork(path)
                mWorking = true
                mBtnPlay.text = "stop"
            }
        }
    }

    private fun showSaveFilePath(path: String) {
        AlertDialog.Builder(this)
            .setTitle("完成！")
            .setMessage("编码后的文件存放在 $path")
            .setPositiveButton(
                "确定"
            ) { dialog, which -> dialog.dismiss() }.show()
    }

    private fun initView() {
        surfaceview = findViewById(R.id.surfaceview)
        mBtnPlay = findViewById(R.id.btnStartVideo)
        mBtnPlay.text = "start"
    }

    private fun startWork(path: String) {
        camera = getBackCamera()
        startcamera(camera)
        h264Encoder = NYH264Encoder(width, height, framerate, biterate, path)
        h264Encoder?.startEncoderThread()
    }
    private fun startcamera(mCamera: Camera?) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(object : Camera.PreviewCallback {
                    override fun onPreviewFrame(data: ByteArray, camera: Camera?) {
                        putYUVData(data)
                    }
                })
                //                mCamera.setDisplayOrientation(90);
                if (parameters == null) {
                    parameters = mCamera.parameters
                }
                parameters = mCamera.parameters
                parameters?.setPreviewFormat(ImageFormat.NV21)
                parameters?.setPreviewSize(width, height)
                mCamera.parameters = parameters
                mCamera.setPreviewDisplay(surfaceHolder)
                mCamera.startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun stopWork() {
        if (null != camera) {
            camera?.setPreviewCallback(null)
            camera?.stopPreview()
            camera?.release()
            camera = null
            h264Encoder?.stopThread()
        }
    }

    private fun supportAvcCodec(): Boolean {
        if (Build.VERSION.SDK_INT >= 18) {
            val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)
            val codecInfos = mediaCodecList.codecInfos
            for (j in codecInfos.size - 1 downTo 0) {
                val codecInfo = codecInfos[j]
                val types = codecInfo.supportedTypes
                for (i in types.indices) {
                    if (types[i].equals("video/avc", ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun getBackCamera(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open(0) // attempt to get a Camera instance
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return c // returns null if camera is unavailable
    }




    private fun putYUVData(buffer: ByteArray) {
        if (NYH264Encoder.YUVQueue.size >= 10) {
            NYH264Encoder.YUVQueue.poll()
        }
        NYH264Encoder.YUVQueue.add(buffer)
    }

}


