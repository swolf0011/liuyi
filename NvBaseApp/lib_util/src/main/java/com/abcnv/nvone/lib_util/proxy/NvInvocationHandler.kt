package com.abcnv.nvone.lib_util.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @Description:
 *
 * @Use:{非必须}
 *
 * @param T
 * @property //implementationClazz Class<T> 需要代理的实现类class
 * @property //proxyMethodHandler IProxyMethodHandler 代理方法的处理
 * @property //proxyMethodNames MutableList<String> 需要被代理的方法名
 * @constructor
 *
 * @Author liuyi

 * @DATE 2024/3/5 14:07
 */
class NvInvocationHandler<T>(
    implementationClazz: Class<T>,
    proxyMethodHandler: IProxyMethodHandler? = null,
    proxyMethodNames: List<String>? = null
) : InvocationHandler {
    var mTarget: T? = null
    var mProxyMethodHandler: IProxyMethodHandler? = null
    val mProxyMethodNames = mutableListOf<String>()

    init {
        mTarget = implementationClazz.getDeclaredConstructor().newInstance()
        mProxyMethodHandler = proxyMethodHandler
        proxyMethodNames?.let {
            mProxyMethodNames.addAll(it)
        }
    }

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
        val methodName = method.name
        return if (mProxyMethodNames.size > 0 && mProxyMethodNames.contains(methodName)) {
            mProxyMethodHandler?.before()
            // 因为传来的参数 args 是不确定的，所以用 *args.orEmpty() 传参
            val result = method.invoke(mTarget, *args.orEmpty())
            mProxyMethodHandler?.after()
            result
        } else {
            // 因为传来的参数 args 是不确定的，所以用 *args.orEmpty() 传参
            method.invoke(mTarget, *args.orEmpty())
        }
    }

    interface IProxyMethodHandler {
        fun before()
        fun after()
    }
}