package com.onekeyads.oneway

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.splash.ISplashAds
import mobi.oneway.export.Ad.OWSplashAd
import mobi.oneway.export.AdListener.OWSplashAdListener
import mobi.oneway.export.enums.OnewaySdkError

private const val TAG = "OnewaySplashAds"
class OnewaySplashAds: ISplashAds() {

    private var splashAd: OWSplashAd? = null

    override fun loadSplash(activity: Activity, savedInstanceState: Bundle?, splashAdsId: String, callBack: (Boolean) -> Unit) {
        splashAd = OWSplashAd(activity, splashAdsId, object: OWSplashAdListener {
            override fun onAdReady() {
                Log.i(TAG, "onAdReady")
                val container = FrameLayout(activity)
                activity.addContentView(container, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT))
                splashAd?.showSplashAd(container)
            }

            override fun onAdShow() {
                Log.i(TAG, "onAdShow")
            }

            override fun onAdError(error: OnewaySdkError?, msg: String?) {
                Log.i(TAG, "onAdError->$error, $msg")
                callBack.invoke(false)
            }

            override fun onAdFinish() {
                Log.i(TAG, "onAdFinish")
                callBack.invoke(true)
            }

            override fun onAdClick() {
                Log.i(TAG, "onAdClick")
            }
        }, 2000)
        splashAd?.loadSplashAd()
    }

    override fun detach(activity: Activity) {
        super.detach(activity)
        splashAd?.destory()
    }
}