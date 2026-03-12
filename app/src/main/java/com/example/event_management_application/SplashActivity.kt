package com.example.event_management_application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.splashLogo)
        val title = findViewById<TextView>(R.id.splashTitle)
        val subtitle = findViewById<TextView>(R.id.splashSubtitle)

        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 1200; fillAfter = true }
        logo.startAnimation(fadeIn)
        title.startAnimation(fadeIn)
        subtitle.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            val nextScreen = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(nextScreen)
            finish()
        }, 2500)
    }
}
