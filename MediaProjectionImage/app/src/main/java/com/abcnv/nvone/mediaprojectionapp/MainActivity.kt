package com.abcnv.nvone.mediaprojectionapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.abcnv.nvone.lib_util.NvFileUtil
import com.abcnv.nvone.mediaprojectionapp.databinding.ActivityMainBinding
import com.abcnv.nvone.mediaprojectionapp.util.NvMediaProjectionUtil
import com.abcnv.nvone.mediaprojectionapp.util.NvBitmapUtil
import com.abcnv.nvone.mediaprojectionapp.util.NvPermissionUtil
import com.abcnv.nvone.mediaprojectionapp.util.Util
import java.io.File


class MainActivity : AppCompatActivity() {
    private var mData: Intent? = null
    private var mNvPermissionUtil = NvPermissionUtil()

    private var pairMediaProjection: Pair<ActivityResultLauncher<Intent>, Intent>? = null

    private var mService: ScreenCaptureService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            if (iBinder is ScreenCaptureService.ScreenCaptureServiceBinder) {
                mService = iBinder.getService()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.buttonStart.setOnClickListener {
            if (mNvPermissionUtil.checkPermission(this, mNvPermissionUtil.PS_EXTERNAL)) {
                NvMediaProjectionUtil.launch(pairMediaProjection)
            }
        }
        viewBinding.buttonSave.setOnClickListener {
            mData?.let {
                startCaptureHandler(it)
            }
        }
        getPermissionOnCreate() {}
        getMediaProjectionToOnCreate()

        getPermissionLaunch()
    }


    private fun getPermissionLaunch() {
        mNvPermissionUtil.launch(mNvPermissionUtil.PS_EXTERNAL)
    }

    private fun getPermissionOnCreate(callback: (Boolean) -> Unit) {
        mNvPermissionUtil.getPermissionToOnCreate(this, mNvPermissionUtil.PS_EXTERNAL) {
            callback.invoke(it)
        }
    }

    private fun getMediaProjectionToOnCreate() {
        pairMediaProjection = NvMediaProjectionUtil.getMediaProjectionToOnCreate(this) { data ->
            mData = data
            if (data == null) {
                Toast.makeText(this, "user cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startCaptureHandler(intent0: Intent) {

        val triple = Util.getScreenWidthHeightDpi(this)
//        val dir = NvFileUtil.getRootNameDir("DCIM/Camera")//Android10的存储权限还没给，这个路径不能用
        val dir = NvFileUtil.getExternalFilesDir(this)
        val file = File(dir, "IMG_${System.currentTimeMillis()}_aa.jpg")
        Thread({
            mService?.startCapture(intent0, triple.first, triple.second) {
                NvBitmapUtil.saveBitmap(file.absolutePath, it)
            }
        }).start()
    }

    override fun onStart() {
        super.onStart()
        val intent0 = Intent(this, ScreenCaptureService::class.java)
        bindService(intent0, connection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // 在这里将BACK键模拟了HOME键的返回桌面功能（并无必要）
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Util.simulateHome(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}