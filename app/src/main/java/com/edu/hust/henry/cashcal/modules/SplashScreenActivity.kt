package com.edu.hust.henry.cashcal.modules

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import com.edu.hust.henry.cashcal.MainActivity
import com.edu.hust.henry.cashcal.R
import com.sdsmdg.harjot.rotatingtext.models.Rotatable
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val typeface_rtt: Typeface = Typeface.createFromAsset(assets, "fonts/UTMAvenida.ttf")
        val typeface_about: Typeface = Typeface.createFromAsset(assets, "fonts/digital-7 (italic).ttf")

        val animationLogo: Animation = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.logo)
        val animationText: Animation = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.bounce)

        custom_switcher.size = 35

        val rotatable = Rotatable(Color.parseColor("#FFFFFF"), 1000,
                getString(R.string.rtt_name), getString(R.string.rtt_convenient),
                getString(R.string.rtt_fast), getString(R.string.rtt_effective),
                getString(R.string.rtt_ahihi))

        rotatable.size = 35F
        rotatable.animationDuration = 500
        rotatable.typeface = typeface_rtt
        rotatable.interpolator = BounceInterpolator()
        custom_switcher.setContent("",rotatable)

        logo_splash.startAnimation(animationLogo)
        custom_switcher.startAnimation(animationText)
        splash_about.typeface = typeface_about

        android.os.Handler().postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            5000)
    }
}
