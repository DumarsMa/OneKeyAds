package com.onekeyads.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.nativead.INativeAd

class  MainActivity : AppCompatActivity() {

    private var nativeAd: INativeAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.onekey_rewarded_btn).setOnClickListener {
            AdsFactory.createRewardedAds().attach(this, AdsConstant.RewardedAdId) {
            }
        }
        findViewById<Button>(R.id.onekey_native_ad_load).setOnClickListener {
            loadNativeAd(findViewById(R.id.onekey_native_ad))
        }
    }

    private fun loadNativeAd(container: FrameLayout) {
        container.removeAllViews()
        nativeAd?.detach(this)
        AdsFactory.createNativeAd().apply {
            nativeAd = this
            loadNativeAd(this@MainActivity, "/6499/example/native") { success, content ->
                if (!success || null == content) {
                    return@loadNativeAd
                }
                container.addView(content.mediaView.getView(), FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT))
                if (content.mediaView.isVideo()) {
                    content.mediaView.playVideo()
                }
            }
        }
    }
}