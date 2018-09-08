package com.edu.hust.henry.cashcal

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.BounceInterpolator
import com.sdsmdg.harjot.rotatingtext.models.Rotatable
import kotlinx.android.synthetic.main.activity_splash_screen.*



class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var typeface1: Typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf")
        var typeface2: Typeface = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf")

        custom_switcher.size = 35
        custom_switcher.typeface = typeface1

        var rotatable: Rotatable = Rotatable(Color.parseColor("#FFA036"), 1000, "BTS", "LilPump", "ChainSmoker")

        rotatable.size = 35F
        rotatable.animationDuration = 500
        rotatable.setTypeface(typeface2)
        rotatable.interpolator = BounceInterpolator()

        custom_switcher.setContent("This is ?", rotatable)
    }
}
