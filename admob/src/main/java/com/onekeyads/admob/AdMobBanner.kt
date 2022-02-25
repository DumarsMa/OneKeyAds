package com.onekeyads.admob

import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.onekeyads.base.view.banner.IBannerView

private const val TAG = "AdMobBanner"
class AdMobBanner: IBannerView {

    override fun attachToBanner(container: FrameLayout, bannerConfig: IBannerView.BannerConfig) {
        container.removeAllViews()
        val adView = AdView(container.context).apply {
            val density = context.resources.displayMetrics.density
            adSize = AdSize.getInlineAdaptiveBannerAdSize((bannerConfig.width / density).toInt(),
                (bannerConfig.height / density).toInt()
            )
            adUnitId = bannerConfig.adsId
        }
        container.addView(adView, FrameLayout.LayoutParams(bannerConfig.width, bannerConfig.height).apply {
            gravity = Gravity.CENTER
        })
        adView.adListener = object: AdListener() {
            override fun onAdClicked() {
                Log.i(TAG, "onAdClicked")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.i(TAG, "onAdClosed")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                Log.i(TAG, "onAdFailedToLoad $error")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.i(TAG, "onAdImpression")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.i(TAG, "onAdLoaded")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.i(TAG, "onAdOpened")
            }
        }
        adView.loadAd(AdRequest.Builder()
            .build())
    }

    override fun detachFromBanner(container: FrameLayout) {
    }
}