package com.onekeyads.huawei

import android.app.Activity
import com.onekeyads.base.view.rewarded.IRewardedAds

class HuaweiRewardedAds: IRewardedAds() {

    override fun loadRewardedAds(
        context: Activity,
        adsId: String,
        callBack: (RewardedResult) -> Unit
    ) {
    }
}