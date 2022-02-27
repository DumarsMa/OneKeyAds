package com.onekeyads.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.onekeyads.base.AdsFactory

class  MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.onekey_rewarded_btn).setOnClickListener {
            AdsFactory.createRewardedAds().attach(this, AdsConstant.RewardedAdId) {
            }
        }
    }
}