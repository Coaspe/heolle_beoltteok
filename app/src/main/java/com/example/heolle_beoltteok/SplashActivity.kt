package com.example.heolle_beoltteok

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.heolle_beoltteok.R

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed ({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },SPLASH_VIEW_TIME)
    }
}