package com.abc_swolf.ly.module_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.abc_swolf.ly.lib_arouter.ARouterUtil
import com.abc_swolf.ly.lib_arouter.IMyProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = ARouterUtil.route_user_activity)
class UserActivity : AppCompatActivity() {
//    kotlin中如果要使用@Autowired注解需要注意：
//    1、变量名称必须要跟传入的key保持一致
//    2、必须使用@JvmField注解修饰
//    3、必须使用var来修饰
    @JvmField
    @Autowired
    var name = ""



    @JvmField
    @Autowired(name = ARouterUtil.route_data_provider)
    var mMyProvider1: IMyProvider? = null
    @JvmField
    @Autowired
    var mMyProvider2: IMyProvider? = null

    var mMyProvider3: IMyProvider? = null
    var mMyProvider4: IMyProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //为了使用ARouter的IProvider。
        ARouter.getInstance().inject(this)


        findViewById<Button>(R.id.button2).setOnClickListener {
            ARouterUtil.navigation(ARouterUtil.route_home_activity,"from UserActivity")
            Toast.makeText(this,name, Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            ARouterUtil.navigationPayActivty(this,"from UserActivity")
            Toast.makeText(this,name, Toast.LENGTH_LONG).show()
        }

        var ls1 = mMyProvider1?.getUserName("1111")
        var ls2 = mMyProvider2?.getUserName("2222")

        mMyProvider3 = ARouterUtil.buildDataProvidery()
        mMyProvider4 = ARouterUtil.navigationDataProvidery()

        var ls3 = mMyProvider1?.getUserName("3333")
        var ls4 = mMyProvider2?.getUserName("4444")


        println("0011 == !${ls1}")
        println("0011 == @${ls2}")

        println("0011 == %${ls3}")
        println("0011 == ^${ls4}")
    }
}