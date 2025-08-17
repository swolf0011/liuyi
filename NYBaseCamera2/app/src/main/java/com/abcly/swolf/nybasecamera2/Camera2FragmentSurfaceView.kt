package com.abcly.swolf.nybasecamera2

import android.Manifest
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.abcly.swolf.nybasecamera2.util.NYCameraDeviceStateCallback

/**
 * A simple [Fragment] subclass.
 * Use the [Camera2FragmentSurfaceView.newInstance] factory method to
 * create an instance of this fragment.
 */
class Camera2FragmentSurfaceView : Fragment() {
    private lateinit var mSurfaceView: SurfaceView
    private lateinit var iv_show: ImageView
    private lateinit var buttonChange: Button
    private lateinit var buttonCapture: Button

    private val mainHandler = Handler(Looper.getMainLooper())


    /**
     * 摄像头创建监听
     */
    private var stateCallback: NYCameraDeviceStateCallback? = null
    val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA] == true &&
                it[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true &&
                it[Manifest.permission.READ_EXTERNAL_STORAGE] == true
            ) {
                //权限全部获取后的操作
                stateCallback?.openCamera(mainHandler)
            } else {
                //没有全部获取的操作
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_camera2_surfaceview, container, false)
        initView(root)
        stateCallback = NYCameraDeviceStateCallback(requireActivity(), mSurfaceView)
        initListener()
        return root
    }

    override fun onResume() {
        super.onResume()
        // 获取手机方向
        val rotation = requireActivity().windowManager.defaultDisplay.rotation
        val lp = mSurfaceView.layoutParams as LinearLayout.LayoutParams
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            lp.width = LinearLayout.LayoutParams.MATCH_PARENT
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
            lp.height = LinearLayout.LayoutParams.MATCH_PARENT
        } else {
            lp.width = LinearLayout.LayoutParams.MATCH_PARENT
            lp.height = LinearLayout.LayoutParams.MATCH_PARENT
        }
        mSurfaceView.layoutParams = lp
    }

    override fun onDestroy() {
        stateCallback?.closeCamera()
        super.onDestroy()
    }

    private fun initView(root: View) {
        iv_show = root.findViewById<ImageView>(R.id.iv_show_camera2)
        buttonCapture = root.findViewById<Button>(R.id.buttonCapture)
        buttonChange = root.findViewById<Button>(R.id.buttonChange)
        //mSurfaceView
        mSurfaceView = root.findViewById<SurfaceView>(R.id.surface_view_camera2)
        mSurfaceView.holder.setKeepScreenOn(true)
        // mSurfaceView添加回调
        mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) { //SurfaceView创建
                // 初始化Camera
                stateCallback?.initCamera2(
                    CameraCharacteristics.LENS_FACING_FRONT,
                    mainHandler
                ) { code, bitmap ->
                    if (code == 0) {
                        val ps = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        requestCameraPermission.launch(ps)
                    } else if (code == 1 && bitmap != null) {
                        iv_show.setImageBitmap(bitmap)
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) { //SurfaceView销毁
                // 释放Camera资源
                //SurfaceView销毁
                stateCallback?.cameraDevice!!.close()
                stateCallback?.cameraDevice = null
            }
        })

    }

    private fun initListener() {
        buttonCapture.setOnClickListener { stateCallback?.takePicture() }
        buttonChange.setOnClickListener {
            stateCallback?.switchCamera(mainHandler) { code, bitmap ->
                if (code == 1 && bitmap != null) {
                    iv_show.setImageBitmap(bitmap)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Camera2FragmentSurfaceView()
    }
}