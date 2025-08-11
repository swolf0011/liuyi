package com.abcnv.nvone.lib_util.handler

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Message

object NvHandlerUtil {
    fun createUIHandler(context: Context,handleMsgCallback:(Message)->Unit): Handler {
        val uiHandler = Handler(context.mainLooper,object :Handler.Callback{
            override fun handleMessage(msg: Message): Boolean {
                println("uiHandler 0011==---- ${msg.what}---${Thread.currentThread().name}")
                handleMsgCallback.invoke(msg)
                return false
            }
        })
        return uiHandler
    }
    fun createHandlerThreadIOHandler(handleMsgCallback:(Message)->Unit): Handler {
        val ht = HandlerThread("ht")
        ht.start()
        val ioHandler =  Handler(ht.looper,object :Handler.Callback{
            override fun handleMessage(msg: Message): Boolean {
                println("ioHandler 0011==---- ${msg.what}---${Thread.currentThread().name}")
                handleMsgCallback.invoke(msg)
                return false
            }
        })
        return ioHandler
    }
}