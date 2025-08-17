package com.swolf.ly.hiltapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/3/13 13:26
 */
@HiltAndroidApp  //@HiltAndroidApp这个必须放在app模块下。
class App :Application() {

}