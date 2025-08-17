package com.swolf.ly.lib_hilt.a01_module

import com.swolf.ly.lib_hilt.a02_model.animal.AnimalCat
import com.swolf.ly.lib_hilt.a02_model.animal.AnimalOx
import com.swolf.ly.lib_hilt.a02_model.animal.IAnimal
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)  //告诉Hilt 这个module属于的Component,ActivityComponent是Hilt定义好的
interface AnimalBindModule {
    /**
     * 如果只一个bindXX方法，@Inject注入时可以使用接口接收。如梦：@Inject lateinit var animalOx0: IAnimal
     * 如果有多个bindXX方法，@Inject注入时只能使用子类接收。如梦：@Inject lateinit var animalOx1: AnimalOx
     * @param animalCat AnimalCat
     * @return IAnimal
     */
    @Binds
    fun bindCat(animalCat: AnimalCat): IAnimal
    @Binds//只能返回实现这个接口的一个子类
    fun bindXo(animalOx: AnimalOx): IAnimal
}