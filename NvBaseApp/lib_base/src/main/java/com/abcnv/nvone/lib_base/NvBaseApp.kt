package com.abcnv.nvone.lib_base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.abcnv.nvone.lib_base.data.NvKVUtil
import dagger.hilt.android.HiltAndroidApp
import java.util.*

/**
 * @Description: APP
 *
 * @Use:{非必须}
 *
 * @property:
 *
 * @Author liuyi
 */
@HiltAndroidApp
class NvBaseApp : Application() {
    var TAG = "0011==${javaClass.simpleName}"

    companion object {
        lateinit var mApp: Application
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
        //生成APP异常处理对象
        NvExceptionHandler.getInstance(this.applicationContext)
        //监听Activity的生命中周期
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)

        NvExceptionHandler.getInstance(this)

        NvKVUtil.initMMKV(this)
    }

    //保存所有Activity
    private val activityStack = Stack<Activity>()

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityStack.push(activity)
            val msg = activity.javaClass.simpleName + "=>Created"
            Log.i(TAG, msg)
        }

        override fun onActivityStarted(activity: Activity) {

            val msg = activity.javaClass.simpleName + "=>Started"
            Log.i(TAG, msg)
        }

        override fun onActivityResumed(activity: Activity) {
            val msg = activity.javaClass.simpleName + "=>Resumed"
            Log.i(TAG, msg)
        }

        override fun onActivityPaused(activity: Activity) {
            val msg = activity.javaClass.simpleName + "=>Paused"
            Log.i(TAG, msg)
        }

        override fun onActivityStopped(activity: Activity) {
            val msg = activity.javaClass.simpleName + "=>Stopped"
            Log.i(TAG, msg)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            val msg = activity.javaClass.simpleName + "=>SaveInstanceState"
            Log.i(TAG, msg)
        }

        override fun onActivityDestroyed(activity: Activity) {
            activityStack.remove(activity)
            val msg = activity.javaClass.simpleName + "=>Destroyed"
            Log.i(TAG, msg)
        }
    }
}