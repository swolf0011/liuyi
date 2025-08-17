package com.abcly.swolf.nybasecamera2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2FragmentSurfaceView.newInstance())
                    .commit();
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, Camera2FragmentSurfaceView.newInstance())
//                .commit()
        }
    }
}