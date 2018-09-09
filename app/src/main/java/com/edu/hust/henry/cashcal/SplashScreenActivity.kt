package com.edu.hust.henry.cashcal

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sdsmdg.harjot.rotatingtext.models.Rotatable
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val typeface1: Typeface = Typeface.createFromAsset(assets, "fonts/Raleway-ExtraBold.ttf")
        val typeface2: Typeface = Typeface.createFromAsset(assets, "fonts/Reckoner_Bold.ttf")

        custom_switcher.size = 35
        custom_switcher.typeface = typeface1

        val rotatable = Rotatable(Color.parseColor("#62FF6D"), 1000, "BTS", "LilPump", "ChainSmoker")

        rotatable.size = 35F
        rotatable.animationDuration = 500
        rotatable.typeface = typeface2
//        rotatable.interpolator = BounceInterpolator()

        custom_switcher.setContent("This is ?", rotatable)

        android.os.Handler().postDelayed(
                { startActivity(Intent(this, MainActivity::class.java))},
                5000)
    }
}
