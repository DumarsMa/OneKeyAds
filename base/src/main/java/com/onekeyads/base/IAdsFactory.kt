package com.onekeyads.base

import android.content.Context
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.base.view.rewarded.IRewardedAds

interface IAdsFactory {

    fun setConfig(config: AdsConfig) {
    }

    fun init(context: Context, callback: (Boolean) -> Unit)

    fun createBannerView(): IBannerView

    fun createSplashAds(): ISplashAds

    fun createRewardedAds(): IRewardedAds
}