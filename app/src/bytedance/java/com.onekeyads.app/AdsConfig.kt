package com.onekeyads.app

import android.content.Context
import com.onekeyads.base.AdsConfig

class AdsConfig(context: Context): AdsConfig("5001121", context.getString(R.string.app_name))

object AdsConstant {
    val SplashAdsId = "801121648"
    //竖屏
//    val RewardedAdId = "901121365"
    //横屏
    val RewardedAdId = "901121430"
}