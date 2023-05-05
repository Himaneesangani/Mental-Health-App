package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashScreenActivity : AppCompatActivity() {
    lateinit var handler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        textAnimation.duration = 3000 // set the duration of the animation
        textAnimation.repeatCount = Animation.INFINITE // set the repeat count of the animation

        val textView = findViewById<TextView>(R.id.splashtv)
        textView.startAnimation(textAnimation)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },4000)
    }
}