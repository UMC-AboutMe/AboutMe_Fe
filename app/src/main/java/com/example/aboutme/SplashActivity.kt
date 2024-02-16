package com.example.aboutme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val globe = findViewById<ImageView>(R.id.splash_globe)
//        val animation = AnimationUtils.loadAnimation(this, R.anim.splash)
//        globe.startAnimation(animation)

        globe.animate().apply {
            duration = 500
            rotationYBy(360f)
        }.start()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000) //3000 : 3초 동안 스프래시 화면 보여주기

    }

}
