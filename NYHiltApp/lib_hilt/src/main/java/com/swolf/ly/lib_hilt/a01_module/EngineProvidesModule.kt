package com.swolf.ly.lib_hilt.a01_module

import com.swolf.ly.lib_hilt.a02_model.ChinaCar
import com.swolf.ly.lib_hilt.a02_model.engine.EngineAmerica
import com.swolf.ly.lib_hilt.a02_model.engine.EngineChina
import com.swolf.ly.lib_hilt.a03_annotation.MadeInCN
import com.swolf.ly.lib_hilt.a03_annotation.MadeInUSA
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class) //告诉Hilt 这个module属于的Component,ActivityComponent是Hilt定义好的
class EngineProvidesModule {

    //假设为第三方提供的，无法通过构造方法上使用@Inject
    @Provides
    @MadeInCN
    fun provideBYDCar(): ChinaCar {
        return ChinaCar(EngineChina())
    }
    //假设为第三方提供的，无法通过构造方法上使用@Inject
    @Provides
    @MadeInUSA
    fun provideFuTeCar(): ChinaCar {
        return ChinaCar(EngineAmerica())
    }

}