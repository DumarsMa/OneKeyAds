package com.onekeyads.admob

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.onekeyads.base.view.rewarded.IRewardedAds

private const val TAG = "AdMobRewardAds"
class AdMobRewardAds: IRewardedAds() {

    override fun loadRewardedAds(
        context: Activity,
        adsId: String,
        callBack: (RewardedResult) -> Unit
    ) {
        RewardedAd.load(context, adsId, createAdRequest(), object : RewardedAdLoadCallback() {

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                Log.i(TAG, "onAdFailedToLoad $error")
                callBack.invoke(RewardedResult.FAIL)
            }
            override fun onAdLoaded(ad: RewardedAd) {
                super.onAdLoaded(ad)
                Log.i(TAG, "onAdLoaded $ad")
                ad.fullScreenContentCallback = createContentCallback(callBack)
                ad.show(context) { rewardItem ->
                    Log.i(TAG, "onUserEarnedReward $rewardItem")
                    callBack.invoke(RewardedResult.REWARDED)
                }
            }
        })
    }

    private fun createContentCallback(callBack: (RewardedResult) -> Unit): FullScreenContentCallback {
        return object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.i(TAG, "onAdClicked")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                Log.i(TAG, "onAdDismissedFullScreenContent")
                callBack.invoke(RewardedResult.CLOSE)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                super.onAdFailedToShowFullScreenContent(error)
                Log.i(TAG, "onAdFailedToShowFullScreenContent $error")
                callBack.invoke(RewardedResult.FAIL)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.i(TAG, "onAdImpression")
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                Log.i(TAG, "onAdShowedFullScreenContent")
            }
        }
    }

    private fun createAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }
}