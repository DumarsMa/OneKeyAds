package com.onekeyads.app

import android.content.Context
import com.onekeyads.base.AdsConfig

class AdsConfig(context: Context): AdsConfig("14851", context.getString(R.string.app_name))

object AdsConstant {
    val SplashAdsId = "video"
}