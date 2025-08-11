package com.abcnv.nvone.lib_arouter

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

/**
 * @Description: 登录拦截器，每一个路由都会通过拦截器。拦截NvARouterPath.route_activity_pay路由
 *
 * @Use:
 *
 * @property
 *
 * @Author liuyi
 */
@Interceptor(priority = 8,name="login")
class NvLoginInterceptor: IInterceptor {
    override fun init(context: Context?) {
        println("0011 == LoginInterceptor init")
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (postcard.path.equals(NvARouterPath.route_activity_pay)) {
            //获取参数
            val name = postcard.extras.getString("name")
            val age = postcard.extras.getInt("age")
            if(NvARouterPath.isLogin){
                callback.onContinue(postcard)
            }else{
                callback.onInterrupt(RuntimeException("Login Exception"))
            }
        }else{
            callback.onContinue(postcard)
        }
    }
}