package com.onekeyads.startapp

import android.app.Activity
import com.onekeyads.base.view.rewarded.IRewardedAds

class StartAppRewardAd: IRewardedAds() {
    override fun loadRewardedAds(
        context: Activity,
        adsId: String,
        callBack: (RewardedResult) -> Unit
    ) {
    }
}