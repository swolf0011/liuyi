package com.abc_swolf.ly.lib_arouter

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

/**
 * @Description: 登录拦截器，每一个路由都会通过拦截器。
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/3/25 0:59
 */
@Interceptor(priority = 8,name="login")
class LoginInterceptor:IInterceptor {
    override fun init(context: Context?) {
        println("0011 == LoginInterceptor init")
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (postcard.getPath().equals(ARouterUtil.route_pay_activity)) {
            if(ARouterUtil.isLogin){
                callback.onContinue(postcard)
            }else{
                callback.onInterrupt(RuntimeException("有点异常"))
            }
        }else{
            callback.onContinue(postcard)
        }
    }
}