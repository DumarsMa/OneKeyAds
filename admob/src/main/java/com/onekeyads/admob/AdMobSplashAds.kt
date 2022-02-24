package com.onekeyads.admob

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.onekeyads.base.view.splash.ISplashAds

private const val TAG = "AdMobSplashAds"
class AdMobSplashAds: ISplashAds() {

    private var loadedAd: AppOpenAd? = null

    override fun loadSplash(activity: Activity, splashAdsId: String, callBack: (Boolean) -> Unit) {
        val orientation = if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AppOpenAd.APP_OPEN_AD_ORIENTATION_LANDSCAPE
        } else {
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT
        }
        AppOpenAd.load(
            activity.applicationContext,
            splashAdsId,
            createAdRequest(),
            orientation,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    super.onAdLoaded(ad)
                    this@AdMobSplashAds.loadedAd = ad
                    showSplashAd(activity, ad, callBack)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.i(TAG, "onAdFailedToLoad $error")
                    callBack.invoke(false)
                }
            }
        )
    }

    private fun showSplashAd(activity: Activity, splashAd: AppOpenAd, callBack: (Boolean) -> Unit) {
        splashAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
            }
        }
    }

    private fun createAdRequest(): AdManagerAdRequest {
        return AdManagerAdRequest()
    }

    override fun onStart(activity: Activity, callBack: ((Boolean) -> Unit)?) {
        super.onStart(activity, callBack)
        callBack?.invoke(true)
    }

    override fun detach(activity: Activity) {
        super.detach(activity)
        loadedAd = null
    }
}