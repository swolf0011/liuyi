package com.abcnv.one.lib_cameraxutil

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.abcnv.one.lib_cameraxutil.databinding.ActivityCameraBinding
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {

    private val ps = if (NvPermissionUtil.isTIRAMISU_33) {
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )

    }

    private val registerForActivityResult =
        NvPermissionUtil.registerRequestMultiplePermissions(this, ps) {
            Log.d("0011==", "3333==${it}")
            startCamera()
        }
    private val activityResultLauncher = NvPermissionUtil.registerStartActivityForResult(this) {
        registerForActivityResult.launch(ps)
    }

    private lateinit var binding: ActivityCameraBinding
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_camera)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageCaptureButton.setOnClickListener { NvCameraXUtil.takePhoto(this) }
        binding.videoCaptureButton.setOnClickListener { captureVideo() }

        if (NvPermissionUtil.isR_30 && !NvPermissionUtil.isExternalStorageManager()) {
            Log.d("0011==", "11111")
            NvPermissionUtil.externalStorageManager(activityResultLauncher)
        } else {
            Log.d("0011==", "222")
            registerForActivityResult.launch(ps)
        }
    }

    private fun startCamera() {
        NvCameraXUtil.startCamera(this, binding.viewFinder.surfaceProvider)
    }

    private fun captureVideo() {
        NvCameraXUtil.captureVideo(this,
            callbackStart = {
                binding.videoCaptureButton.apply {
                    text = getString(R.string.stop_capture)
                }
            },
            callbackFinalize = {
                binding.videoCaptureButton.apply {
                    text = getString(R.string.start_capture)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        NvCameraXUtil.stopCamera()
        cameraExecutor.shutdown()
    }
}