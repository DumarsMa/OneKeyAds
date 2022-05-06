package com.onkeyads.factory

import android.content.Context
import com.applovin.sdk.AppLovinSdk
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds

object AdsFactoryImpl : IAdsFactory {
    override fun init(context: Context, callback: (Boolean) -> Unit) {
        val sdk = AppLovinSdk.getInstance(context.applicationContext)
        if (sdk.isInitialized) {
            callback.invoke(true)
            return
        }
        sdk.mediationProvider = "max"
        sdk.initializeSdk { configuration ->
            callback.invoke(true)
        }
    }

    override fun createBannerView(): IBannerView {
        TODO("Not yet implemented")
    }

    override fun createSplashAds(): ISplashAds {
        TODO("Not yet implemented")
    }

    override fun createRewardedAds(): IRewardedAds {
        TODO("Not yet implemented")
    }

    override fun createNativeAd(): INativeAd {
        return super.createNativeAd()
    }
}