package com.abcly.swolf.nyaidlclientapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.abcly.swolf.nyaidlserviceapp.INYAidlInterface

class MainActivity : AppCompatActivity() {
    var mAidlInterface: INYAidlInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mAidlInterface = INYAidlInterface.Stub.asInterface(service)
                mAidlInterface?.let {
                    val str = it.sendMsg("深圳欢迎您!")
                    println("0011 == 客户端收到服务器数据：${str}")
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }

        val intent0 = Intent()
        intent0.setAction("com.jideos.module_usercenter.service.UserCenterService.action")
        intent0.setPackage("com.abcly.swolf.nyaidlserviceapp")
        bindService(intent0,connection, BIND_AUTO_CREATE)

    }
}