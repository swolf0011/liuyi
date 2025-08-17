package com.abc_swolf.ly.module_home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.abc_swolf.ly.lib_arouter.ARouterUtil
import com.abc_swolf.ly.lib_arouter.IMyProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = ARouterUtil.route_home_activity)
class HomeActivity : AppCompatActivity() {

//    kotlin中如果要使用@Autowired注解需要注意：
//    1、变量名称必须要跟传入的key保持一致
//    2、必须使用@JvmField注解修饰
//    3、必须使用var来修饰
    @JvmField
    @Autowired
    var name = "123"

    @JvmField
    @Autowired(name = ARouterUtil.route_data_provider)
    var mMyProvider: IMyProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.button2).setOnClickListener {
            ARouterUtil.navigationPayActivty(this,"from HomeActivity")
            Toast.makeText(this, name, Toast.LENGTH_LONG).show()
            println("0011 == =name:${mMyProvider?.getUserName("mmmmm")}")
            var p = ARouterUtil.navigationDataProvidery()
            println("0011 == =name:${p?.getUserName("mmmmm")}")
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            ARouterUtil.navigation(ARouterUtil.route_user_activity, "from HomeActivity")
            Toast.makeText(this, name, Toast.LENGTH_LONG).show()
        }
    }
}