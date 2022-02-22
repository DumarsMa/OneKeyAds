package com.onekeyads.bytedance

import android.app.Activity
import com.onekeyads.base.view.rewarded.IRewardedAds

class RewardAds: IRewardedAds {

    override fun attach(
        context: Activity,
        config: String?,
        callBack: (IRewardedAds.RewardedResult) -> Unit
    ) {
    }

    override fun detach(context: Activity) {
    }
}