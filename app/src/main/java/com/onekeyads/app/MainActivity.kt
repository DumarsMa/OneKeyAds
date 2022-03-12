package com.onekeyads.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.nativead.NativeAdView

class  MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.onekey_rewarded_btn).setOnClickListener {
            AdsFactory.createRewardedAds().attach(this, AdsConstant.RewardedAdId) {
            }
        }
        findViewById<Button>(R.id.onekey_native_ad_load).setOnClickListener {
            findViewById<NativeAdView>(R.id.onekey_native_ad).load()
        }
    }
}