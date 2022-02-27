package com.onekeyads.unity

import android.app.Activity
import android.util.Log
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions

private const val TAG = "UnityRewardedAds"
class UnityRewardedAds: IRewardedAds() {

    override fun loadRewardedAds(
        context: Activity,
        adsId: String,
        callBack: (RewardedResult) -> Unit
    ) {
        UnityAds.load(adsId, object: IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placement: String?) {
                Log.i(TAG, "onUnityAdsAdLoaded->$placement")
                showAd(context, adsId, callBack)
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String?,
                error: UnityAds.UnityAdsLoadError?,
                msg: String?
            ) {
                Log.i(TAG, "onUnityAdsFailedToLoad->$placementId, $error, $msg")
                callBack.invoke(IRewardedAds.RewardedResult.FAIL)
            }

        })
    }

    private fun showAd(activity: Activity, placementId: String, callBack: (IRewardedAds.RewardedResult) -> Unit) {
        UnityAds.show(activity, placementId, UnityAdsShowOptions(), object: IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String?,
                error: UnityAds.UnityAdsShowError?,
                msg: String?
            ) {
                Log.i(TAG, "onUnityAdsShowFailure->$placementId, $error, $msg")
                callBack.invoke(IRewardedAds.RewardedResult.FAIL)
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
                callBack.invoke(IRewardedAds.RewardedResult.REWARDED)
            }
        })
    }
}