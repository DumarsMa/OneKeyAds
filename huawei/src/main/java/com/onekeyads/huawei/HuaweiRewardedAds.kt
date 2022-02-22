package com.onekeyads.huawei

import android.app.Activity
import com.onekeyads.base.view.rewarded.IRewardedAds

class HuaweiRewardedAds: IRewardedAds {
    override fun attach(
        context: Activity,
        config: String?,
        callBack: (IRewardedAds.RewardedResult) -> Unit
    ) {
    }

    override fun detach(context: Activity) {
    }
}