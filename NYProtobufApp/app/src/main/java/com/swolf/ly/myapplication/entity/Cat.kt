package com.swolf.ly.myapplication.entity

import com.swolf.ly.myapplication.MyProtobuf

data class Cat(var id: Long = 0, var name: String = "", var type: Int = 1, var age: Int = 1){
    override fun toString(): String {
        return "${id},${name},${type},${age}."
    }
}
object CatConverter {
    var protobufByteArray:ByteArray? = null
    var protobufStr = ""
    fun protobuf2Cat(): Cat {
        val cat = Cat()
        if(UserConverter.protobufByteArray == null){
            return cat
        }
        try {
            val catWrapper = MyProtobuf.CatWrapper.parseFrom(protobufByteArray)
            cat.id = catWrapper.id
            cat.name = catWrapper.name
            cat.type = catWrapper.type
            cat.age = catWrapper.age
        }catch (e:Exception){
            e.printStackTrace()
        }
        return cat
    }
    fun cat2protobuf( cat:Cat): String {
        val builder = MyProtobuf.CatWrapper.newBuilder()
        builder.id = cat.id
        builder.name = cat.name
        builder.type = cat.type
        builder.age = cat.age
        val catWrapper = builder.build()
        val byteString = catWrapper.toByteString()
        protobufByteArray = catWrapper.toByteArray()
        protobufStr = byteString.toString()
        return protobufStr
    }
}