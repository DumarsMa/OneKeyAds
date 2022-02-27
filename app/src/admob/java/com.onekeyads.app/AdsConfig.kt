package com.onekeyads.app

import android.content.Context
import com.onekeyads.base.AdsConfig

class AdsConfig(context: Context): AdsConfig("", context.getString(R.string.app_name))

object AdsConstant {
    val SplashAdsId = "ca-app-pub-3940256099942544/3419835294"
    val RewardedAdId = "ca-app-pub-3940256099942544/5224354917"
}