package com.swolf.ly.lib_hilt.a01_module

import com.swolf.ly.lib_hilt.a02_model.animal.AnimalCat
import com.swolf.ly.lib_hilt.a02_model.animal.AnimalOx
import com.swolf.ly.lib_hilt.a02_model.animal.IAnimal
import com.swolf.ly.lib_hilt.a03_annotation.MadeInCat
import com.swolf.ly.lib_hilt.a03_annotation.MadeInOx
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)  //告诉Hilt 这个module属于的Component,ActivityComponent是Hilt定义好的
class AnimalProvidesModule {
    //返回多个不同子类
    @Provides
    @MadeInCat
    fun provideCat(): IAnimal {
        return AnimalCat()
    }
    //返回多个不同子类
    @Provides
    @MadeInOx
    fun provideXo(): IAnimal {
        return AnimalOx()
    }

}