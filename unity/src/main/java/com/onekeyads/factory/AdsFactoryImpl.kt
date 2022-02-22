package com.onekeyads.factory

import android.content.Context
import com.onekeyads.base.AdsConfig
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.unity.UnityBannerView
import com.onekeyads.unity.UnityRewardedAds
import com.onekeyads.unity.UnitySplashAds
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds

class AdsFactoryImpl: IAdsFactory, IUnityAdsInitializationListener {

    private lateinit var adsConfig: AdsConfig
//    private var gameId: String = "4559967"

    private val callbacks = mutableListOf<(Boolean) -> Unit>()
    private var isIniting = false

    override fun setConfig(config: AdsConfig) {
        adsConfig = config
    }

    override fun init(context: Context, callback: (Boolean) -> Unit) {
        assert(this::adsConfig.isInitialized)
        if (UnityAds.isInitialized()) {
            callback.invoke(true)
            return
        }
        callbacks.add(callback)
        if (isIniting) {
            return
        }
        isIniting = true
        UnityAds.initialize(context.applicationContext, adsConfig.appId, adsConfig.debuggable, this)
    }

    override fun onInitializationComplete() {
        isIniting = false
        callbacks.forEach {
            it.invoke(true)
        }
        callbacks.clear()
    }

    override fun onInitializationFailed(p0: UnityAds.UnityAdsInitializationError?, p1: String?) {
        isIniting = false
        callbacks.forEach {
            it.invoke(false)
        }
        callbacks.clear()
    }

    override fun createBannerView(): IBannerView {
        return UnityBannerView()
    }

    override fun createSplashAds(): ISplashAds {
        return UnitySplashAds()
    }

    override fun createRewardedAds(): IRewardedAds {
        return UnityRewardedAds()
    }
}