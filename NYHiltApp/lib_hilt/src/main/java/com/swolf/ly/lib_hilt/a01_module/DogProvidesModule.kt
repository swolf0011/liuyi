package com.swolf.ly.lib_hilt.a01_module

import com.swolf.ly.lib_hilt.a02_model.Dog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class) //告诉Hilt 这个module属于的Component,ActivityComponent是Hilt定义好的
class DogProvidesModule {
    //假设为第三方提供的，无法通过构造方法上使用@Inject
    @Provides
    fun provideDog() = Dog("京巴犬")
}