package com.abcnv.one.lib_camerax

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object CameraXTakePhotoUtil {

    var imageCapture: ImageCapture? = null
    var cameraSelector: CameraSelector? = null
    var camera: Camera? = null
    var cameraExecutor: ExecutorService? = null
    var lensFacing = CameraSelector.LENS_FACING_BACK

    fun initCameraX(
        applicationContext: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        screenWidth: Int,
        screenHeight: Int
    ) {
        lensFacing = CameraSelector.LENS_FACING_BACK
        cameraExecutor = Executors.newSingleThreadExecutor()
//        val cameraController = LifecycleCameraController(applicationContext)
//        cameraController.bindToLifecycle(lifecycleOwner)
//        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//        previewView.controller = cameraController
        val cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderFuture.addListener({
            bindPreviewOfTakePhoto(
                applicationContext,
                cameraProviderFuture,
                lifecycleOwner,
                previewView,
                screenWidth,
                screenHeight
            )
        }, ContextCompat.getMainExecutor(applicationContext))
        previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
    }

    fun bindPreviewOfTakePhoto(
        applicationContext: Context,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        screenWidth: Int,
        screenHeight: Int
    ) {
        try {
            val screenAspectRatio = aspectRatio(screenWidth, screenHeight)



            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            val preview = Preview.Builder().setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(previewView.display.rotation).build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(previewView.display.rotation)
                .setFlashMode(ImageCapture.FLASH_MODE_OFF)//闪光灯
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(previewView.display.rotation)
                .build().also {
                    it.setAnalyzer(cameraExecutor!!, { luma ->
                        Log.d("0011==", "Average luminosity: $luma")
                    })
                }

            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            if (camera != null) {
                // Must remove observers from the previous camera instance
                camera!!.cameraInfo.cameraState.removeObservers(lifecycleOwner)

            }
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector!!,
                imageCapture,
                imageAnalyzer,
                preview
            )

            observeCameraState(applicationContext, lifecycleOwner, camera!!.cameraInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closeCameraX() {
        imageCapture = null
        cameraSelector = null
        camera = null
    }

    fun takePhoto(context: Context, imageCapture: ImageCapture, path: String) {
        val saveFile = File(path)
        if (!saveFile.parentFile!!.exists()) {
            saveFile.parentFile!!.mkdirs()
        }
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(saveFile).build()

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // var savedUri = outputFileResults.getSavedUri()
                    Toast.makeText(context, "保存成功: ${saveFile.absolutePath}", Toast.LENGTH_SHORT)
                        .show()
                    closeCameraX()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - 4.0 / 3.0) <= abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun observeCameraState(
        applicationContext: Context,
        lifecycleOwner: LifecycleOwner,
        cameraInfo: CameraInfo
    ) {
        cameraInfo.cameraState.observe(lifecycleOwner) { cameraState ->
            run {
                when (cameraState.type) {
                    CameraState.Type.PENDING_OPEN -> {
                        // Ask the user to close other camera apps
                        Toast.makeText(
                            applicationContext,
                            "CameraState: Pending Open",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.Type.OPENING -> {
                        // Show the Camera UI
                        Toast.makeText(
                            applicationContext,
                            "CameraState: Opening",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.Type.OPEN -> {
                        // Setup Camera resources and begin processing
                        Toast.makeText(
                            applicationContext,
                            "CameraState: Open",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.Type.CLOSING -> {
                        // Close camera UI
                        Toast.makeText(
                            applicationContext,
                            "CameraState: Closing",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.Type.CLOSED -> {
                        // Free camera resources
                        Toast.makeText(
                            applicationContext,
                            "CameraState: Closed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            cameraState.error?.let { error ->
                when (error.code) {
                    // Open errors
                    CameraState.ERROR_STREAM_CONFIG -> {
                        // Make sure to setup the use cases properly
                        Toast.makeText(
                            applicationContext,
                            "Stream config error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Opening errors
                    CameraState.ERROR_CAMERA_IN_USE -> {
                        // Close the camera or ask user to close another camera app that's using the
                        // camera
                        Toast.makeText(
                            applicationContext,
                            "Camera in use",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.ERROR_MAX_CAMERAS_IN_USE -> {
                        // Close another open camera in the app, or ask the user to close another
                        // camera app that's using the camera
                        Toast.makeText(
                            applicationContext,
                            "Max cameras in use",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.ERROR_OTHER_RECOVERABLE_ERROR -> {
                        Toast.makeText(
                            applicationContext,
                            "Other recoverable error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Closing errors
                    CameraState.ERROR_CAMERA_DISABLED -> {
                        // Ask the user to enable the device's cameras
                        Toast.makeText(
                            applicationContext,
                            "Camera disabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    CameraState.ERROR_CAMERA_FATAL_ERROR -> {
                        // Ask the user to reboot the device to restore camera function
                        Toast.makeText(
                            applicationContext,
                            "Fatal error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Closed errors
                    CameraState.ERROR_DO_NOT_DISTURB_MODE_ENABLED -> {
                        // Ask the user to disable the "Do Not Disturb" mode, then reopen the camera
                        Toast.makeText(
                            applicationContext,
                            "Do not disturb mode enabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}