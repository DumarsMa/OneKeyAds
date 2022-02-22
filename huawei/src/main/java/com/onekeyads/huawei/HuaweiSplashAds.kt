package com.onekeyads.huawei

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.AudioFocusType
import com.huawei.hms.ads.splash.SplashAdDisplayListener
import com.huawei.hms.ads.splash.SplashView
import com.onekeyads.base.view.splash.ISplashAds

private const val TAG = "HuaweiSplashAds"
class HuaweiSplashAds: ISplashAds {

    private var handler: Handler? = null
    private var splashAds: SplashView? = null

    override fun attach(activity: Activity, config: String, callBack: (Boolean) -> Unit) {
        delayInvokeFail(callBack)
        splashAds = SplashView(activity).apply {
            activity.addContentView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
            audioFocusType = AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE
            setAdDisplayListener(object: SplashAdDisplayListener() {
                override fun onAdShowed() {
                    super.onAdShowed()
                    Log.i(TAG, "onAdShowed")
                }

                override fun onAdClick() {
                    super.onAdClick()
                    Log.i(TAG, "onAdClick")
                }
            })
            load(config, activity.resources.configuration.orientation,
                AdParam.Builder().build(),
                object: SplashView.SplashAdLoadListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        cancelDelayInvokeFail()
                    }

                    override fun onAdFailedToLoad(error: Int) {
                        super.onAdFailedToLoad(error)
                        Log.i(TAG, "onAdFailedToLoad $error")
                        callBack.invoke(false)
                    }

                    override fun onAdDismissed() {
                        super.onAdDismissed()
                        Log.i(TAG, "onAdDismissed")
                        callBack.invoke(true)
                    }
                })
        }
    }

    private fun delayInvokeFail(callBack: (Boolean) -> Unit) {
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(Runnable {
            callBack.invoke(false)
        }, 2000)
    }

    private fun cancelDelayInvokeFail() {
        handler?.removeCallbacksAndMessages(null)
    }

    override fun detach(activity: Activity) {
        cancelDelayInvokeFail()
    }
}