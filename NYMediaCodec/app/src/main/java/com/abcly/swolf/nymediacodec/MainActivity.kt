package com.abcly.swolf.nymediacodec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.abcly.swolf.nymediacodec.mediaCodecUtil.NYFileUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val file = NYFileUtil.getExternalStorageDirectory()
        println("0011 == file:${file.absolutePath}")

        findViewById<Button>(R.id.but_encode).setOnClickListener {
            val intent = Intent(this,H264EncoderActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.but_decode).setOnClickListener {
            val intent = Intent(this,H264DecoderActivity::class.java)
            startActivity(intent)
        }
    }
}