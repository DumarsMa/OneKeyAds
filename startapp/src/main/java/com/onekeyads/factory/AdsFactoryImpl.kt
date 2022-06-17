package com.onekeyads.factory

import android.content.Context
import com.onekeyads.base.AdsConfig
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.startapp.StartAppBanner
import com.onekeyads.startapp.StartIONativeAd
import com.onekeyads.startapp.StartAppRewardAd
import com.onekeyads.startapp.StartAppSplashAd
import com.startapp.sdk.adsbase.StartAppSDK

object AdsFactoryImpl: IAdsFactory {

    override fun setConfig(config: AdsConfig) {
        super.setConfig(config)
        StartAppSDK.setTestAdsEnabled(config.debuggable)
    }

    override fun init(context: Context, callback: (Boolean) -> Unit) {
    }

    override fun createBannerView(): IBannerView {
        return StartAppBanner()
    }

    override fun createSplashAds(): ISplashAds {
        return StartAppSplashAd()
    }

    override fun createRewardedAds(): IRewardedAds {
        return StartAppRewardAd()
    }

    override fun createNativeAd(): INativeAd {
        return StartIONativeAd()
    }
}