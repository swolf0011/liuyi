package com.abcly.swolf.nymessengerclientapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent0 = Intent("com.abcly.swolf.nymessengerserviceapp.NYMessengerService")
        intent0.setPackage("com.abcly.swolf.nymessengerserviceapp")
        bindService(intent0, connection, Context.BIND_AUTO_CREATE)

        findViewById<Button>(R.id.button).setOnClickListener {
//            for(i in 0..2283){
                send(i)
//            }
        }
    }
    private fun send(keyCode:Int){
        val bundle = Bundle()
        bundle.putInt("keyCode",  keyCode)
        bundle.putString("content", "你好深圳!${i++}")
        bundle.putString("time", "${System.currentTimeMillis()}")

        val msg = Message.obtain()
        msg.what = 1
        msg.data = bundle
        msg.replyTo = Messenger(handler)
        messenger?.send(msg)
    }


    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val keyCode = msg.data.getInt("keyCode")
                val content = msg.data.getString("content")
                val time = msg.data.getString("time")
                println("0011 == 收到（服务器）返回的数据 === ： ${keyCode}, ${content}, ${time}")
            }
        }
    }

    var messenger: Messenger? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
//            val message = Message.obtain()
//            val b = Bundle()
//            b.putString("client", "客户端连接服务端------")
//            message.data = b
//            message.replyTo = messenger
//            try {
//                messenger?.send(message)
//            } catch (e: RemoteException) {
//                e.printStackTrace()
//            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}