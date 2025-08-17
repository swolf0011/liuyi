package com.swolf.ly.hiltapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.swolf.ly.lib_hilt.DemoActivity
import com.swolf.ly.lib_hilt.NyLog
import com.swolf.ly.lib_hilt.a02_model.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var mActivity:AppCompatActivity
    @Inject
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivity = this
        user.age = 25
        user.name = "zs"
        NyLog.p(user.toString())
        Toast.makeText(this,user.toString(),Toast.LENGTH_LONG).show()


        findViewById<Button>(R.id.button).setOnClickListener {
            var intent = Intent(mActivity,DemoActivity::class.java)
            startActivity(intent)
        }
    }
}