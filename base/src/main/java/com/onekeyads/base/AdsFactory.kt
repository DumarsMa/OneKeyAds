package com.onekeyads.base

import android.content.Context
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.base.view.rewarded.IRewardedAds

object AdsFactory: IAdsFactory {

    private val adsFactoryImpl: IAdsFactory = Class.forName("com.onekeyads.factory.AdsFactoryImpl")
        .newInstance() as IAdsFactory

    override fun setConfig(adsConfig: AdsConfig) {
        adsFactoryImpl.setConfig(adsConfig)
    }

    override fun init(context: Context, callback: (Boolean) -> Unit) {
        adsFactoryImpl.init(context, callback)
    }

    override fun createBannerView(): IBannerView {
        return adsFactoryImpl.createBannerView()
    }

    override fun createSplashAds(): ISplashAds {
        return adsFactoryImpl.createSplashAds()
    }

    override fun createRewardedAds(): IRewardedAds {
        return adsFactoryImpl.createRewardedAds()
    }

    override fun createNativeAd(): INativeAd {
        return adsFactoryImpl.createNativeAd()
    }
}