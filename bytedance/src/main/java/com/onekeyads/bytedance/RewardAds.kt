package com.onekeyads.bytedance

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import com.bytedance.sdk.openadsdk.*
import com.onekeyads.base.view.rewarded.IRewardedAds

private const val TAG = "bytedanceRewardedAds"
class RewardAds: IRewardedAds() {

    override fun loadRewardedAds(
        context: Activity,
        adsId: String,
        callBack: (RewardedResult) -> Unit
    ) {
        val adNative = TTAdSdk.getAdManager().createAdNative(context)
        adNative.loadRewardVideoAd(createAdSlot(context, adsId), object : TTAdNative.RewardVideoAdListener {
            override fun onError(type: Int, msg: String?) {
                Log.i(TAG, "loadAds error $type, $msg")
                callBack.invoke(RewardedResult.FAIL)
            }

            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd?) {
                Log.i(TAG, "loadAds onRewardVideoAdLoad")
                if (null == ad) {
                    callBack.invoke(RewardedResult.FAIL)
                    return
                }
                showAd(context, ad, callBack)
            }

            override fun onRewardVideoCached() {
                Log.i(TAG, "onRewardVideoCached")
            }

            override fun onRewardVideoCached(ad: TTRewardVideoAd?) {
                Log.i(TAG, "onRewardVideoCached")
            }
        })
    }

    private fun showAd(activity: Activity, ad: TTRewardVideoAd, callBack: (RewardedResult) -> Unit) {
        ad.setRewardAdInteractionListener(createRewardAdInteractionListener(callBack))
        ad.showRewardVideoAd(activity)
    }

    private fun createRewardAdInteractionListener(callBack: (RewardedResult) -> Unit): TTRewardVideoAd.RewardAdInteractionListener {
        return object: TTRewardVideoAd.RewardAdInteractionListener {
            override fun onAdShow() {
                Log.i(TAG, "onAdShow")
            }

            override fun onAdVideoBarClick() {
                Log.i(TAG, "onAdVideoBarClick")
            }

            override fun onAdClose() {
                Log.i(TAG, "onAdClose")
                callBack.invoke(RewardedResult.CLOSE)
            }

            override fun onVideoComplete() {
                Log.i(TAG, "onVideoComplete")
            }

            override fun onVideoError() {
                Log.i(TAG, "onVideoError")
            }

            override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String?, errorCode: Int, errorMsg: String?) {
                Log.i(TAG, "onRewardVerify->rewardVerify:$rewardVerify, rewardAmount:$rewardAmount, rewardName:$rewardName," +
                        " errorCode:$errorCode, errorMsg:$errorMsg")
                if (rewardVerify) {
                    callBack.invoke(RewardedResult.REWARDED)
                } else {
                    callBack.invoke(RewardedResult.FAIL)
                }
            }

            override fun onSkippedVideo() {
                Log.i(TAG, "onSkippedVideo")
            }
        }
    }
    private fun createAdSlot(activity: Activity, codeId: String): AdSlot {
        val orientation = if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TTAdConstant.ORIENTATION_LANDSCAPE
        } else {
            TTAdConstant.ORIENTATION_VERTICAL
        }
        return AdSlot.Builder()
            .setCodeId(codeId)
            .setOrientation(orientation)
            .setAdLoadType(TTAdLoadType.PRELOAD)
            .build()
    }
}