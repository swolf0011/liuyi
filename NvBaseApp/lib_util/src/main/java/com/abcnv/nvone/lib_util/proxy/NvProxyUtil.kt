package com.abcnv.nvone.lib_util.proxy
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

object NvProxyUtil {
    /**
     *
     * @param //interfaceClass Class<T> 接口的class
     * @param //invocationHandler InvocationHandler
     * @return  T 返回接口代理对象
     */
    fun <T> proxy(interfaceClass: Class<T>, invocationHandler: InvocationHandler): T {
        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(
            interfaceClass.classLoader,//接口的classLoader
            arrayOf(interfaceClass),//接口的class
            invocationHandler
        ) as T
    }


}