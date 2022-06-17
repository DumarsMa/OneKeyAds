package com.onekeyads.startapp

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.onekeyads.base.view.splash.ISplashAds
import com.startapp.sdk.ads.splash.SplashConfig
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.model.AdPreferences

private const val TAG = "StartAppSplashAd"
class StartAppSplashAd: ISplashAds() {

    private val handler = Handler(Looper.getMainLooper())
    private var timeoutCallback: Runnable? = null
    override fun loadSplash(activity: Activity, savedInstanceState: Bundle?,
                            splashAdsId: String, callBack: (Boolean) -> Unit) {
        val splashConfig = SplashConfig()
            .setOrientation(
                SplashConfig.Orientation.AUTO
            )
        timeoutCallback?.apply {
            handler.removeCallbacks(this)
        }
        timeoutCallback = Runnable {
            callBack.invoke(false)
        }
        handler.postDelayed(timeoutCallback!!, 3000)
        StartAppAd.showSplash(activity, savedInstanceState, splashConfig, AdPreferences()) {
            Log.i(TAG, "showSplash complete")
            callBack.invoke(true)
        }
    }

    override fun onStop(activity: Activity, callBack: ((Boolean) -> Unit)?) {
        super.onStop(activity, callBack)
        timeoutCallback?.apply {
            handler.removeCallbacks(this)
        }
    }

    override fun detach(activity: Activity) {
        super.detach(activity)
        timeoutCallback?.apply {
            handler.removeCallbacks(this)
        }
    }
}