package com.abc_swolf.ly.app02_base_router

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.abc_swolf.ly.lib_arouter.ARouterUtil
import com.abc_swolf.ly.lib_arouter.IMyProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter


class MainActivity : AppCompatActivity() {

    @JvmField
    @Autowired(name = ARouterUtil.route_data_provider)
    var mMyProvider: IMyProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //为了使用ARouter的IProvider。
        ARouter.getInstance().inject(this)


        findViewById<Button>(R.id.button2).setOnClickListener {
//            var name = ARouterUtil.navigationDataProvidery().getUserName("vvvv")
            println("0011 == =name:${mMyProvider?.getUserName("bbbb")}")
            ARouterUtil.navigation(ARouterUtil.route_home_activity,"from MainActivity")
//            this.finish()
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            ARouterUtil.navigationPayActivty(this,"from MainActivity")
//            this.finish()
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            ARouterUtil.navigation(ARouterUtil.route_user_activity,"from MainActivity")
//            this.finish()
        }
    }
}