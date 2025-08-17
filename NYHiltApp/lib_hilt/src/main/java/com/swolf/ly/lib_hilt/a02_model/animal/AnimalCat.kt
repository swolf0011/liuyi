package com.swolf.ly.lib_hilt.a02_model.animal

import com.swolf.ly.lib_hilt.NyLog
import javax.inject.Inject

class AnimalCat@Inject constructor(): IAnimal {
    override fun cry() {
        NyLog.p("喵喵~~~~~~~~~")
    }
}