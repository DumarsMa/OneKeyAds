package com.onekeyads.factory

import android.content.Context
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.onekeyads.base.AdsConfig
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.bytedance.BannerView
import com.onekeyads.bytedance.RewardAds
import com.onekeyads.bytedance.SplashAds

private const val TAG = "AdsFactoryImpl"

class AdsFactoryImpl : IAdsFactory {

    private lateinit var config: AdsConfig

    override fun setConfig(config: AdsConfig) {
        this.config = config
    }
    override fun init(context: Context, callback: (Boolean) -> Unit) {
        assert(this::config.isInitialized)
        if (TTAdSdk.isInitSuccess()) {
            callback.invoke(true)
            return
        }
        TTAdSdk.init(context.applicationContext,
            TTAdConfig.Builder()
                .appId(config.appId)
                .debug(config.debuggable)
//                .appName(config.appName)
                .useTextureView(true)
                .supportMultiProcess(false)
                .needClearTaskReset()
                .asyncInit(false)
                .build(),
            object : TTAdSdk.InitCallback {
                override fun success() {
                    Log.i(TAG, "initSuccess")
                    callback.invoke(true)
                }

                override fun fail(code: Int, msg: String?) {
                    Log.i(TAG, "initFail $code, $msg")
                    callback.invoke(false)
                }
            })
    }

    override fun createBannerView(): IBannerView {
        return BannerView()
    }

    override fun createSplashAds(): ISplashAds {
        return SplashAds()
    }

    override fun createRewardedAds(): IRewardedAds {
        return RewardAds()
    }
}