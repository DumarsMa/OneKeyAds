package com.onekeyads.bytedance

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.onekeyads.base.view.splash.ISplashAds

private const val TAG = "SplashAds"
class SplashAds: ISplashAds() {

    override fun loadSplash(activity: Activity, savedInstanceState: Bundle?, splashAdsId: String, callBack: (Boolean) -> Unit) {
        TTAdSdk.getAdManager().createAdNative(activity)
            .loadSplashAd(createAdSlot(activity, splashAdsId), object: TTAdNative.SplashAdListener {
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
                    splashAd.setSplashInteractionListener(createSplashInteractionListener(callBack))
                    activity.addContentView(view, ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT))
                }
            }, 3500)
    }

    private fun createAdSlot(activity: Activity, codeId: String): AdSlot {
        val displayMetrics = activity.resources.displayMetrics
        val decorView = activity.window.decorView
        val widthPixels = if (decorView.width > 0) {
            decorView.width
        } else {
            displayMetrics.widthPixels
        }
        val heightPixels = if (decorView.height > 0) {
            decorView.height
        } else {
            displayMetrics.heightPixels
        }
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

    override fun onStart(activity: Activity, callBack: ((Boolean) -> Unit)?) {
        super.onStart(activity, callBack)
        callBack?.invoke(true)
    }
}
