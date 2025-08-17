package com.swolf.ly.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.swolf.ly.myapplication.entity.Cat
import com.swolf.ly.myapplication.entity.CatConverter
import com.swolf.ly.myapplication.entity.User
import com.swolf.ly.myapplication.entity.UserConverter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_u2p).setOnClickListener {
            val user = User()
            user.id = 1
            user.name = "ly"
            user.pwd = "123456"
            user.isVip = true
            user.age = 25
            user.likes.add("学习")
            user.likes.add("游泳")
            user.likes.add("爬山")
            UserConverter.user2protobuf(user)
            println("0011 == ${UserConverter.protobufStr}")
            Toast.makeText(this,"${UserConverter.protobufStr}",Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.button_p2u).setOnClickListener {
            val user = UserConverter.protobuf2User()
            println("0011 == ${user}")
            Toast.makeText(this,"${user}",Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.button_c2p).setOnClickListener {
            val cat = Cat()
            cat.id = 1
            cat.name = "Tom"
            cat.type = 2
            cat.age = 25
            CatConverter.cat2protobuf(cat)
            println("0011 == ${CatConverter.protobufStr}")
            Toast.makeText(this,"${CatConverter.protobufStr}",Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.button_p2c).setOnClickListener {
            val cat = CatConverter.protobuf2Cat()
            println("0011 == ${cat}")
            Toast.makeText(this,"${cat}",Toast.LENGTH_LONG).show()
        }
    }
}