package com.swolf.ly.lib_hilt.a02_model

import com.swolf.ly.lib_hilt.NyLog
import com.swolf.ly.lib_hilt.a02_model.engine.IEngine
import javax.inject.Inject

class ChinaCar @Inject constructor(val IEngine: IEngine) {
    var name:String = ""

    fun run(){
        IEngine.on()
        NyLog.p("${name} Run")
    }
    fun stop(){
        IEngine.off()
        NyLog.p("${name} Stop")
    }
}