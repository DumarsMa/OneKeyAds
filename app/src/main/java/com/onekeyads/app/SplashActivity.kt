package com.onekeyads.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onekeyads.base.AdsConfig
import com.onekeyads.base.AdsFactory

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AdsFactory.setConfig(
            AdsConfig("5001121", "test").apply {
                debuggable = true
            }
        )
        AdsFactory.createSplashAds().attach(this, "801121648") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}