package com.onekeyads.factory

import android.content.Context
import com.onekeyads.base.AdsConfig
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.oneway.OnewayBannerView
import com.onekeyads.oneway.OnewayRewardedAds
import com.onekeyads.oneway.OnewaySplashAds
import mobi.oneway.export.Ad.OnewaySdk

class AdsFactoryImpl: IAdsFactory {

    private lateinit var config: AdsConfig

    override fun setConfig(config: AdsConfig) {
        this.config = config
    }

    override fun init(context: Context, callback: (Boolean) -> Unit) {
        kotlin.assert(this::config.isInitialized)
        if (OnewaySdk.isConfigured()) {
            callback.invoke(true)
            return
        }
        OnewaySdk.configure(context.applicationContext, config.appId)
        OnewaySdk.setDebugMode(config.debuggable)
        callback.invoke(true)
    }

    override fun createBannerView(): IBannerView {
        return OnewayBannerView()
    }

    override fun createSplashAds(): ISplashAds {
        return OnewaySplashAds()
    }

    override fun createRewardedAds(): IRewardedAds {
        return OnewayRewardedAds()
    }
}