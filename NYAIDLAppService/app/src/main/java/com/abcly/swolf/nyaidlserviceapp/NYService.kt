package com.abcly.swolf.nyaidlserviceapp

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NYService : Service() {
    init {
        println("0011 == NYService服务器启动了......")
    }

    override fun onBind(intent: Intent): IBinder {
        return NYBinder()
    }
    class NYBinder: INYAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String
        ) {
            println("0011 == 服务器收到客户端basicTypes信息：${anInt}，${aLong}，${aBoolean}，${aFloat}，${aDouble}，${aString}，")
        }

        override fun sendMsg(msg: String): String {
            println("0011 == 服务器收到客户端信息：${msg}")
            return "你好！朋友！"
        }
    }
}