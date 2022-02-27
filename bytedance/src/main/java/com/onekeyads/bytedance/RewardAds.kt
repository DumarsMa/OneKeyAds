package com.onekeyads.bytedance

import android.app.Activity
import com.onekeyads.base.view.rewarded.IRewardedAds

class RewardAds: IRewardedAds() {
    override fun loadRewardedAds(
        context: Activity,
        adsId: String,
        callBack: (RewardedResult) -> Unit
    ) {
    }
}