package com.abcly.swolf.nymessengerserviceapp

import android.app.Service
import android.content.Intent
import android.os.*
import java.util.*

class NYMessengerService : Service() {
    companion object {
        @Volatile
        private var isStopHandler = false

        @Volatile
        private var isThreadRunning = false

        private val queue = LinkedList<NYData>()
    }

    init {
        println("0011 == init NYMessengerService-----------------")
        if (!isThreadRunning) {
            joinService()

        }
    }

    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val keyCode = msg.data.getInt("keyCode")
                val content = msg.data.getString("content") ?: ""
                val time = msg.data.getString("time")
                println("0011 == 收到（客户端）信息： ${keyCode}, ${content}, ${time}")
//                if (keyCode == 0) {
//                    joinService()
//                    return
//                }
//                if (keyCode == -1) {
//                    //退出服务器
//                    exitService()
//                    return
//                }
                if (isThreadRunning) {
                    queue.add(NYData(keyCode, content, msg.replyTo))
                }
            }
        }
    }
    private fun joinService() {
        isStopHandler = false
        isThreadRunning = false
        handlerQueueData()
    }
    private fun exitService() {
        //退出服务器
        isStopHandler = true
        isThreadRunning = false
        queue.clear()
    }

    @Synchronized
    private fun handlerQueueData() {
        val thread = Thread() {
            isThreadRunning = true
            while (!isStopHandler) {
                if (queue.isEmpty()) {
                    Thread.sleep(500)
                    println("0011 == 服务器等待中!!! --- $isStopHandler")
                } else {
                    val data = queue.removeFirst()
                    if(data != null){
                        val bundle = Bundle()
                        bundle.putInt("keyCode", data.keyCode)
                        bundle.putString("content", "我是服务器返回的信息,服务器已收到你的信息")
                        bundle.putString("time", "${System.currentTimeMillis()}")
                        val msg0 = Message.obtain(null, 1)
                        msg0.recycle()
                        msg0.what = 1
                        msg0.data = bundle
                        data.messenger.send(msg0)
                    }
//                    Thread.sleep(10)
                }
            }
            isThreadRunning = false
            println("0011 == 服务器挂了!!! 88")

        }
        thread.start()
    }

    override fun onBind(intent: Intent): IBinder {
        return Messenger(handler).binder
    }
}