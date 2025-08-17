package com.swolf.ly.lib_hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.swolf.ly.lib_hilt.a02_model.ChinaCar
import com.swolf.ly.lib_hilt.a02_model.Dog
import com.swolf.ly.lib_hilt.a02_model.User
import com.swolf.ly.lib_hilt.a02_model.animal.AnimalCat
import com.swolf.ly.lib_hilt.a02_model.animal.AnimalOx
import com.swolf.ly.lib_hilt.a02_model.animal.IAnimal
import com.swolf.ly.lib_hilt.a03_annotation.MadeInCN
import com.swolf.ly.lib_hilt.a03_annotation.MadeInCat
import com.swolf.ly.lib_hilt.a03_annotation.MadeInOx
import com.swolf.ly.lib_hilt.a03_annotation.MadeInUSA
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {



    @Inject
    lateinit var user: User
    @Inject
    lateinit var dog: Dog

    @Inject @MadeInCN
    lateinit  var bydCar: ChinaCar
    @Inject @MadeInUSA
    lateinit var futeCar: ChinaCar

    @Inject
    lateinit var animalOx: AnimalOx
    @Inject
    lateinit var animalCat: AnimalCat

    @Inject @MadeInOx
    lateinit var animalInOx: IAnimal
    @Inject @MadeInCat
    lateinit var animalInCat: IAnimal

    lateinit var mActivity:DemoActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        mActivity = this

        findViewById<Button>(R.id.buttonUser).setOnClickListener {
            NyLog.p(user)
            Toast.makeText(this,user.toString(), Toast.LENGTH_LONG).show()

            user.name = "zs"
            user.age = 30
            user.show()

        }

        findViewById<Button>(R.id.buttonBydRun).setOnClickListener {
            bydCar.name = "BYD"
            bydCar.run()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.buttonBydStop).setOnClickListener {
            bydCar.stop()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.buttonFuTeRun).setOnClickListener {
            futeCar.name = "Fu bu shi"
            futeCar.run()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.buttonFuTeStop).setOnClickListener {
            futeCar.stop()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.buttonAnimalOx).setOnClickListener {
            animalOx.cry()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.buttonAnimalCat).setOnClickListener {
            animalCat.cry()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.buttonAnimalInOx).setOnClickListener {
            animalInOx.cry()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.buttonAnimalInCat).setOnClickListener {
            animalInCat.cry()
            Toast.makeText(this,"0011 ==看日志", Toast.LENGTH_LONG).show()
        }
        findViewById<Button>(R.id.buttonDog).setOnClickListener {
            NyLog.p(dog.name)
            Toast.makeText(this,dog.name, Toast.LENGTH_LONG).show()
        }
    }
}