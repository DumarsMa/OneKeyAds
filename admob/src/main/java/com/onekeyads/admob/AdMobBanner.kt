package com.onekeyads.admob

import android.util.DisplayMetrics
import android.util.Log
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.onekeyads.base.view.banner.IBannerView

private const val TAG = "AdMobBanner"
class AdMobBanner: IBannerView {

    override fun attachToBanner(container: FrameLayout, adsId: String, carousel: Boolean) {
        container.removeAllViews()
        container.post {
            val adView = AdView(container.context).apply {
                val density = context.resources.displayMetrics.density
                adSize = AdSize.getInlineAdaptiveBannerAdSize((container.width / density).toInt(),
                    (container.height / density).toInt()
                )
                adUnitId = adsId
            }
            container.addView(adView)
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
    }

    override fun detachFromBanner(container: FrameLayout) {
    }
}