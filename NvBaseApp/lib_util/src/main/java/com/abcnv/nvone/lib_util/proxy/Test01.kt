package com.abcnv.nvone.lib_util.proxy

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2024/3/5 11:50
 */
interface IAnimal {
    fun run()
    fun eat():Int
    fun read(msg:String):String
}
class Dog: IAnimal {
    override fun run() {
        println("dog is running")
    }

    override fun eat():Int {
        println("dog is eating")
        return 2
    }

    override fun read(msg: String):String {
        println("dog read ${msg}")
        return "222"
    }
}
class Cat: IAnimal {
    override fun run() {
        println("cat is running")
    }

    override fun eat():Int {
        println("cat is eating")
        return 1
    }

    override fun read(msg: String):String {
        println("cat read ${msg}")
        return "111"
    }
}
fun main() {
    val clazz = Dog::class.java
    val methods = mutableListOf<String>()
    methods.add("run")
    methods.add("read")

    val invocationHandler = NvInvocationHandler(clazz,object:
        NvInvocationHandler.IProxyMethodHandler {
        override fun before() {
            println("IProxyMethod    before----")
        }

        override fun after() {
            println("IProxyMethod    after========")
        }
    },methods)

    val proxyObj = NvProxyUtil.proxy(IAnimal::class.java, invocationHandler)

    proxyObj.run()
    val r0 = proxyObj.eat()
    val r1 = proxyObj.read("book11")
    println(r0)
    println(r1)
}