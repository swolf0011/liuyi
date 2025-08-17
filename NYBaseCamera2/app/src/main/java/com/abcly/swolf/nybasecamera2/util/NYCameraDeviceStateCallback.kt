package com.abcly.swolf.nybasecamera2.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/4/27 14:21
 */
class NYCameraDeviceStateCallback(
    var mActivity: Activity,
    var mSurfaceView: SurfaceView
) : CameraDevice.StateCallback() {

    var cameraDevice: CameraDevice? = null

    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var previewSize: Size? = null
    private var childHandler: Handler? = null
    private var mCameraID: String = ""

    //获取摄像头管理
    private val cameraManager = mActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val imageReader = ImageReader.newInstance(1920, 1920, ImageFormat.JPEG, 1)
    private val ORIENTATIONS = SparseIntArray()
    private val sessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(captureSession: CameraCaptureSession) {
            // 当摄像头已经准备好时，开始显示预览
            cameraCaptureSession = captureSession
            initSessionStateCallbackConfigured(captureSession)
        }

        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
            Toast.makeText(mActivity, "配置失败", Toast.LENGTH_SHORT).show()
        }
    }
    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)

        val handlerThread = HandlerThread("Camera2")
        handlerThread.start()
        childHandler = Handler(handlerThread.looper)
    }

    override fun onOpened(camera: CameraDevice) { //打开摄像头
        cameraDevice = camera
        takePreview()
    }

    override fun onDisconnected(camera: CameraDevice) { //关闭摄像头
        cameraDevice?.close()
        cameraDevice = null
    }

    override fun onError(camera: CameraDevice, error: Int) { //发生错误
        Toast.makeText(mActivity, "摄像头开启失败", Toast.LENGTH_SHORT).show()
    }
    /**
     *
     */
    private fun setPreviewRequestBuilderVal() {
        // 自动对焦
        val afModeKey = CaptureRequest.CONTROL_AF_MODE
        val afModeVal = CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
        previewRequestBuilder?.set<Int>(afModeKey, afModeVal)
        // 打开闪光灯
        val aeModeKey = CaptureRequest.CONTROL_AE_MODE
        val aeModeVal = CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
        previewRequestBuilder?.set<Int>(aeModeKey, aeModeVal)
    }

    /**
     *
     */
    private fun initSessionStateCallbackConfigured(cameraCaptureSession:CameraCaptureSession) {
        // 显示预览
        if (previewRequestBuilder == null) {
            return
        }
        setPreviewRequestBuilderVal()
        val previewRequest = previewRequestBuilder!!.build()
        cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler)
    }

    /**
     * 预览
     */
    private fun takePreview() {
        if (cameraDevice == null) {
            return
        }
        // 创建预览需要的CaptureRequest.Builder
        previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        // 将SurfaceView的surface作为CaptureRequest.Builder的目标
        previewRequestBuilder?.addTarget(mSurfaceView.holder.surface)
        // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
        val ls = listOf(mSurfaceView.holder.surface, imageReader.surface)
        cameraDevice?.createCaptureSession(ls, sessionStateCallback, childHandler)
    }

    /**
     * 拍照
     */
    fun takePicture() {
        if (cameraDevice == null) return
        // 创建拍照需要的CaptureRequest.Builder
        val captureRequestBuilder =
            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        // 将imageReader的surface作为CaptureRequest.Builder的目标
        captureRequestBuilder.addTarget(imageReader.surface)
        setPreviewRequestBuilderVal()
        // 获取手机方向
        val rotation = mActivity.windowManager.defaultDisplay.rotation
        val lp = mSurfaceView.layoutParams as LinearLayout.LayoutParams
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        mSurfaceView.layoutParams = lp
        // 根据设备方向计算设置照片的方向
        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
        //拍照
        val mCaptureRequest = captureRequestBuilder.build()
        cameraCaptureSession?.capture(mCaptureRequest, null, childHandler)
    }

    /**
     * 切换摄像头
     *
     * @param mainHandler Handler
     * @param callback Function2<Int, Bitmap?, Unit>
     */
    fun switchCamera(mainHandler: Handler, callback: (Int, Bitmap?) -> Unit) {
        val back = CameraCharacteristics.LENS_FACING_BACK
        val front = CameraCharacteristics.LENS_FACING_FRONT
        val key = CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(key)
            val maxSize = getMaxSize(map!!.getOutputSizes(SurfaceHolder::class.java))
            val characteristic = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (mCameraID == "" + back && characteristic == front) {
                //前置转后置
                previewSize = maxSize
                cameraDevice?.close()
                initCamera2(front, mainHandler, callback)
                break
            } else if (mCameraID == "" + front && characteristic == back) {
                //后置转前置
                previewSize = maxSize
                cameraDevice?.close()
                initCamera2(back, mainHandler, callback)
                break
            }
        }
    }

    /**
     * 初始化相机
     *
     * @param cameraID Int
     * @param mainHandler Handler
     * @param callback Function2<Int, Bitmap?, Unit>
     */
    fun initCamera2(cameraID: Int, mainHandler: Handler, callback: (Int, Bitmap?) -> Unit) {
//        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;
        mCameraID = "${cameraID}"
        imageReader.setOnImageAvailableListener({ reader ->
            //可以在这里处理拍照得到的临时照片 例如，写入本地。当图片可得到的时候获取图片并保存
            //mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
            //mCameraDevice.close();
            // 拿到拍照照片数据
            val image = reader.acquireNextImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer[bytes] //由缓冲区存入字节数组
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            if (bitmap != null) {
                callback.invoke(1, bitmap)
            }
            cameraDevice?.close()
        }, mainHandler)

        val ps = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        for (p in ps) {
            val result = ActivityCompat.checkSelfPermission(mActivity, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                callback.invoke(0, null)//申请WRITE_EXTERNAL_STORAGE权限
                return
            }
        }
        //打开摄像头
        cameraManager.openCamera("${cameraID}", this, mainHandler)
    }

    /**
     * 打开相机
     * @param mainHandler Handler
     */
    fun openCamera(mainHandler: Handler) {
        if(mCameraID.isEmpty()){
            return
        }
        try {
            cameraManager.openCamera(mCameraID, this, mainHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    fun closeCamera(){
        //关闭捕捉会话
        cameraCaptureSession?.close()
        //关闭相机
        cameraDevice?.close()
        //关闭拍照处理器
        imageReader.close()
    }
    /**
     * 获取最大预览尺寸
     * @param outputSizes Array<Size>
     * @return Size?
     */
    private fun getMaxSize(outputSizes: Array<Size>): Size? {
        if (outputSizes.isEmpty()) {
            return null
        }
        var sizeMax = outputSizes[0]
        for (size in outputSizes) {
            if (size.width * size.height > sizeMax.width * sizeMax.height) {
                sizeMax = size
            }
        }
        println("0011 == sizeMax::${sizeMax.width},${sizeMax.height}")
        return sizeMax
    }
}