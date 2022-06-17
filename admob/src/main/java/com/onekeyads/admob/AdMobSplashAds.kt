package com.onekeyads.admob

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.onekeyads.base.view.splash.ISplashAds

private const val TAG = "AdMobSplashAds"
class AdMobSplashAds: ISplashAds() {

    private val handler = Handler(Looper.getMainLooper())
    private var loadedAd: AppOpenAd? = null

    override fun loadSplash(activity: Activity, savedInstanceState: Bundle?, splashAdsId: String, callBack: (Boolean) -> Unit) {
        Log.i(TAG, "start loadSplash")
        delayInvokeResult(false, callBack)
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
                    Log.i(TAG, "onAdLoaded")
                    if (!contextValid()) {
                        return
                    }
                    handler.removeCallbacksAndMessages(null)
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
                handler.removeCallbacksAndMessages(null)
                Log.i(TAG, "onAdDismissedFullScreenContent")
                callBack.invoke(true)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                super.onAdFailedToShowFullScreenContent(error)
                Log.i(TAG, "onAdFailedToShowFullScreenContent $error")
                handler.removeCallbacksAndMessages(null)
                callBack.invoke(false)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                Log.i(TAG, "onAdShowedFullScreenContent")
//                delayInvokeResult(true, callBack)
            }
        }
        splashAd.show(activity)
    }

    private fun createAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    override fun onStart(activity: Activity, callBack: ((Boolean) -> Unit)?) {
        super.onStart(activity, callBack)
        callBack?.invoke(true)
    }

    private fun delayInvokeResult(result: Boolean = false, callBack: (Boolean) -> Unit) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(Runnable {
            callBack.invoke(result)
        }, 3000)
    }

    override fun detach(activity: Activity) {
        super.detach(activity)
        handler.removeCallbacksAndMessages(null)
        loadedAd = null
    }
}