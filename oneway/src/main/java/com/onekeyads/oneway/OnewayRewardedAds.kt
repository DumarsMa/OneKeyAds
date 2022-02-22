package com.onekeyads.oneway

import android.app.Activity
import android.util.Log
import com.onekeyads.base.view.rewarded.IRewardedAds
import mobi.oneway.export.Ad.OWRewardedAd
import mobi.oneway.export.AdListener.OWRewardedAdListener
import mobi.oneway.export.enums.OnewayAdCloseType
import mobi.oneway.export.enums.OnewaySdkError

private const val TAG = "OnewayRewardedAds"
class OnewayRewardedAds: IRewardedAds {

    private var rewardedAds: OWRewardedAd? = null
    override fun attach(context: Activity, config: String?, callBack: (IRewardedAds.RewardedResult) -> Unit) {
        rewardedAds?.destory()
        rewardedAds = OWRewardedAd(context, config, object: OWRewardedAdListener {
            override fun onAdReady() {
                Log.i(TAG, "onAdReady")
                rewardedAds?.show(context)
            }

            override fun onAdShow(placementId: String?) {
                Log.i(TAG, "onAdShow")
            }

            override fun onAdClick(placementId: String?) {
                Log.i(TAG, "onAdClick $placementId")
            }

            override fun onAdClose(placementId: String?, type: OnewayAdCloseType?) {
                Log.i(TAG, "onAdClose $placementId, $type")
                callBack.invoke(IRewardedAds.RewardedResult.CLOSE)
            }

            override fun onAdFinish(placementId: String?, type: OnewayAdCloseType?, msg: String?) {
                Log.i(TAG, "onAdFinish->$placementId, $type, $msg")
                callBack.invoke(IRewardedAds.RewardedResult.REWARDED)
            }

            override fun onSdkError(error: OnewaySdkError?, msg: String?) {
                Log.i(TAG, "onSdkError->$error, $msg")
                callBack.invoke(IRewardedAds.RewardedResult.FAIL)
            }
        })
    }

    override fun detach(context: Activity) {
        rewardedAds?.destory()
    }
}