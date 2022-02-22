package com.onekeyads.base.view.rewarded

import android.app.Activity

interface IRewardedAds {

    enum class RewardedResult {
        FAIL,
        REWARDED,
        CLOSE
    }
    fun attach(context: Activity, config: String? = null, callBack: (RewardedResult) -> Unit)

    fun detach(context: Activity)
}