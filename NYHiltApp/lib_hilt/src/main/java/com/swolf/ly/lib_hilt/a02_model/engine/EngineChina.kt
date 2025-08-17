package com.swolf.ly.lib_hilt.a02_model.engine

import com.swolf.ly.lib_hilt.NyLog
import javax.inject.Inject

class EngineChina @Inject constructor(): IEngine {
    override fun on() {
        NyLog.p("ChinaEngine on")
    }

    override fun off() {
        NyLog.p("ChinaEngine off")
    }
}