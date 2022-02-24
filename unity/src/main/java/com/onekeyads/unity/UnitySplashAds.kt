package com.onekeyads.unity

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.splash.ISplashAds
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions

private const val TAG = "UnityInterstitialAds"
class UnitySplashAds: ISplashAds() {

    override fun loadSplash(activity: Activity, splashAdsId: String, callBack: (Boolean) -> Unit) {
        UnityAds.load(splashAdsId, object: IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placement: String?) {
                Log.i(TAG, "onUnityAdsAdLoaded->$placement")
                showAd(activity, splashAdsId, callBack)
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String?,
                error: UnityAds.UnityAdsLoadError?,
                msg: String?
            ) {
                Log.i(TAG, "onUnityAdsFailedToLoad->$placementId, $error, $msg")
                callBack.invoke(false)
            }

        })
    }

    private fun showAd(activity: Activity, placementId: String, callBack: (Boolean) -> Unit) {
        UnityAds.show(activity, placementId, UnityAdsShowOptions(), object: IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String?,
                error: UnityAds.UnityAdsShowError?,
                msg: String?
            ) {
                Log.i(TAG, "onUnityAdsShowFailure->$placementId, $error, $msg")
                callBack.invoke(false)
            }

            override fun onUnityAdsShowStart(placementId: String?) {
                Log.i(TAG, "onUnityAdsShowStart $placementId")
            }

            override fun onUnityAdsShowClick(placementId: String?) {
                Log.i(TAG, "onUnityAdsShowClick $placementId")
            }

            override fun onUnityAdsShowComplete(
                placementId: String?,
                state: UnityAds.UnityAdsShowCompletionState?
            ) {
                Log.i(TAG, "onUnityAdsShowComplete $placementId $state")
                callBack.invoke(true)
            }
        })
    }
}