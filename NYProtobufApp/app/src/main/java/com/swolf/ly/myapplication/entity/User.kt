package com.swolf.ly.myapplication.entity

import com.swolf.ly.myapplication.MyProtobuf

data class User(
    var id: Long = 0,
    var name: String = "",
    var pwd: String = "",
    var isVip: Boolean = false,
    var age: Int = 1,
    var likes:MutableList<String> = mutableListOf()
){
    override fun toString(): String {
        val ls = StringBuffer()
        likes.forEach {
            ls.append(it)
        }
        return "${id},${name},${pwd},${isVip},${age},${ls}."
    }
}


object UserConverter {

    var protobufByteArray:ByteArray? = null
    var protobufStr = ""
    fun protobuf2User(): User {
        val user = User()
        if(protobufByteArray == null){
            return user
        }
        try {
            val userWrapper = MyProtobuf.UserWrapper.parseFrom(protobufByteArray)
            user.id = userWrapper.id
            user.name = userWrapper.name
            user.pwd = userWrapper.pwd
            user.isVip = userWrapper.isVip
            user.age = userWrapper.age
            userWrapper.likesList.forEach {
                user.likes.add(it)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return user
    }
    fun user2protobuf(user:User): String {
        val builder = MyProtobuf.UserWrapper.newBuilder()
        builder.id = user.id
        builder.name = user.name
        builder.pwd = user.pwd
        builder.isVip = user.isVip
        builder.age = user.age
        user.likes.forEach {
            builder.addLikes(it)
        }
        val userWrapper = builder.build()
        val byteString = userWrapper.toByteString()
        protobufByteArray = userWrapper.toByteArray()
        protobufStr = byteString.toString()
        return protobufStr
    }
}