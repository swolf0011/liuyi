package com.abcnv.nvone.mediaprojectionapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.abcnv.nvone.mediaprojectionapp.util.NvMediaProjectionUtil
import com.abcnv.nvone.mediaprojectionapp.util.NvPermissionUtil
import com.abcnv.nvone.mediaprojectionapp.util.Util


class MainActivity : AppCompatActivity() {
    private var mNvPermissionUtil = NvPermissionUtil()
    lateinit var buttonStart: Button
    lateinit var buttonStop: Button




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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)

        buttonStart.setOnClickListener {
            getPermissionLaunch()
        }
        buttonStop.setOnClickListener {
            stopRecorderScreen()
        }
        getPermissionOnCreate() {}
        getMediaProjectionToOnCreate()

        val intent0 = Intent(this, ScreenCaptureService::class.java)
        bindService(intent0, connection, BIND_AUTO_CREATE)
    }
    private fun getPermissionLaunch() {
        mNvPermissionUtil.launch(mNvPermissionUtil.PS_EXTERNAL)
    }
    private fun getPermissionOnCreate(callback: (Boolean) -> Unit) {
        mNvPermissionUtil.getPermissionToOnCreate(this, mNvPermissionUtil.PS_EXTERNAL) {
            callback.invoke(it)
            NvMediaProjectionUtil.launch(pairMediaProjection)
        }
    }
    private var mData: Intent? = null
    private var pairMediaProjection: Pair<ActivityResultLauncher<Intent>, Intent>? = null

    private fun getMediaProjectionToOnCreate() {
        pairMediaProjection = NvMediaProjectionUtil.getMediaProjectionToOnCreate(this) { data ->
            mData = data
            if (data == null) {
                Toast.makeText(this, "user cancelled", Toast.LENGTH_LONG).show()
            }else{
                startRecorderScreen(mData!!)
            }
        }
    }
//    private fun checkPermission() {
//        val pair0 = mNvPermissionUtil.checkPermission(this) {
//            if (mIntent != null) {
//                startRecorderScreen(mIntent!!)
//                return@checkPermission
//            }
//            activityResultLauncher1.launch(intent1)
//        }
//        activityResultLauncher0 = pair0.first
//        ps0 = pair0.second
//    }

//    private fun initMediaProjection() {
//
//        val pair1 = MediaProjectionUtil.initMediaProjection(this, callbackSuccess = {
//            mIntent = it
//            startRecorderScreen(it)
//        }, callbackFail = {
//            Toast.makeText(this, "user cancelled", Toast.LENGTH_LONG).show();
//        })
//        activityResultLauncher1 = pair1.first
//        intent1 = pair1.second
//    }

    private fun startRecorderScreen(intent0: Intent) {
        val triple = Util.getScreenWidthHeightDpi(this)
        var width = triple.first / 2
        width = if (width % 2 == 0) width else width + 1
        var height = triple.second / 2
        height = if (height % 2 == 0) height else height + 1
        val densityDpi = triple.third
        val path = NvMediaProjectionUtil.getPath(this)
        mService?.startRecorderScreen(intent0, path, width, height, densityDpi)
        println("0011==startRecorderScreen---------------------------")
    }

    private fun stopRecorderScreen() {
        println("0011==stopRecorderScreen---------------------")
        mService?.stopRecorderScreen()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        unbindService(connection)
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