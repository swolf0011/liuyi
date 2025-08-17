package com.swolf.ly.lib_hilt.a02_model.engine

import com.swolf.ly.lib_hilt.NyLog
import javax.inject.Inject

class EngineAmerica @Inject constructor(): IEngine {
    override fun on() {
        NyLog.p("AmericaEngine on")
    }

    override fun off() {
        NyLog.p("AmericaEngine off")
    }
}