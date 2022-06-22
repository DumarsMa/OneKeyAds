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
    private var splashResult: Boolean? = null
    private var isActivityResumed: Boolean = false
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
        Log.i(TAG, "loadSplash")
        StartAppAd.showSplash(activity, savedInstanceState, splashConfig, AdPreferences()) {
            if (!contextValid()) {
                Log.i(TAG, "showSplash complete but activity invalid")
                return@showSplash
            }
            Log.i(TAG, "showSplash complete $isActivityResumed")
            splashResult = true
            if (isActivityResumed) {
                callBack.invoke(true)
            }
        }
    }

    override fun onPause(activity: Activity, callBack: ((Boolean) -> Unit)?) {
        super.onPause(activity, callBack)
        isActivityResumed = false
        Log.i(TAG, "onPause")
        timeoutCallback?.apply {
            handler.removeCallbacks(this)
        }
    }

    override fun onResume(activity: Activity, callBack: ((Boolean) -> Unit)?) {
        super.onResume(activity, callBack)
        Log.i(TAG, "onResume $splashResult")
        isActivityResumed = true
        splashResult?.apply {
            callBack?.invoke(this)
        }
    }

    override fun detach(activity: Activity) {
        super.detach(activity)
        handler.removeCallbacksAndMessages(null)
    }
}