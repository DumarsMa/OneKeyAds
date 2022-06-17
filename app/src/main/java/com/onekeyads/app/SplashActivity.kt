package com.onekeyads.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.onekeyads.base.AdsFactory

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AdsFactory.createSplashAds().attach(this, savedInstanceState,  AdsConstant.SplashAdsId) {
            Log.i("StartAppSplashAd", "splash result $it")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}