package com.onekeyads.bytedance

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.splash.ISplashAds

private const val TAG = "SplashAds"
class SplashAds: ISplashAds {

    private var callBack: ((Boolean) -> Unit)? = null

    override fun attach(activity: Activity, splashAdsId: String, callBack: (Boolean) -> Unit) {
        AdsFactory.init(activity) { success ->
            Log.i(TAG, "initResult $success")
            if (!success) {
                callBack.invoke(false)
            } else {
                loadSplashAds(activity, splashAdsId, callBack)
            }
        }
    }

    private fun loadSplashAds(activity: Activity, config: String, callBack: (Boolean) -> Unit) {
        TTAdSdk.getAdManager().createAdNative(activity)
            .loadSplashAd(createAdSlot(activity, config), object: TTAdNative.SplashAdListener {
                override fun onError(code: Int, msg: String?) {
                    Log.i(TAG, "loadSplashAds error $code, $msg")
                    callBack.invoke(false)
                }

                override fun onTimeout() {
                    Log.i(TAG, "loadSplashAds timeout")
                    callBack.invoke(false)
                }

                override fun onSplashAdLoad(splashAd: TTSplashAd?) {
                    val view = splashAd?.splashView
                    if (null == view) {
                        Log.i(TAG, "onSplashAdLoad view is null")
                        callBack.invoke(false)
                        return
                    }
                    this@SplashAds.callBack = callBack
                    splashAd.setSplashInteractionListener(createSplashInteractionListener(callBack))
                    activity.addContentView(view, ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT))
                }
            }, 3500)
    }

    private fun createAdSlot(context: Context, codeId: String): AdSlot {
        val displayMetrics = context.resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels
        val scale = displayMetrics.density
        var widthDp = widthPixels + 0.5f
        var heightDp = heightPixels + 0.5f
        if (scale > 0) {
            widthDp = widthPixels / scale + 0.5f
            heightDp = heightPixels / scale + 0.5f
        }
        return AdSlot.Builder()
            .setCodeId(codeId)
            .setExpressViewAcceptedSize(widthDp, heightDp)
            .setImageAcceptedSize(widthPixels, heightPixels)
            .setAdLoadType(TTAdLoadType.PRELOAD)
            .build()
    }

    private fun createSplashInteractionListener(callBack: (Boolean) -> Unit): TTSplashAd.AdInteractionListener {
        return object : TTSplashAd.AdInteractionListener {
            override fun onAdClicked(view: View?, type: Int) {
                Log.i(TAG, "onAdClicked $type")
            }

            override fun onAdShow(view: View?, type: Int) {
                Log.i(TAG, "onAdShow $type")
            }

            override fun onAdSkip() {
                Log.i(TAG, "onAdSkip")
                callBack.invoke(true)
            }

            override fun onAdTimeOver() {
                Log.i(TAG, "onAdTimeOver")
                callBack.invoke(true)
            }
        }
    }

    override fun onResume(activity: Activity) {
        super.onResume(activity)
        callBack?.invoke(true)
    }

    override fun detach(activity: Activity) {
        callBack = null
    }
}