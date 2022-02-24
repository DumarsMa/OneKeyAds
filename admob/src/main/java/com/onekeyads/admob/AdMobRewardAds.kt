package com.onekeyads.admob

import android.app.Activity
import com.onekeyads.base.view.rewarded.IRewardedAds

class AdMobRewardAds: IRewardedAds {
    override fun attach(
        context: Activity,
        config: String?,
        callBack: (IRewardedAds.RewardedResult) -> Unit
    ) {
    }

    override fun detach(context: Activity) {
    }
}