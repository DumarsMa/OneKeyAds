package com.onekeyads.factory

import android.content.Context
import com.huawei.hms.ads.HwAds
import com.onekeyads.base.AdsConfig
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds
import com.onekeyads.huawei.HuaweiBannerView
import com.onekeyads.huawei.HuaweiRewardedAds
import com.onekeyads.huawei.HuaweiSplashAds

class AdsFactoryImpl: IAdsFactory {

    private var hasInit = false

    override fun setConfig(config: AdsConfig) {
    }

    override fun init(context: Context, callback: (Boolean) -> Unit) {
        if (hasInit) {
            callback.invoke(true)
            return
        }
        HwAds.init(context.applicationContext)
        hasInit = true
        callback.invoke(true)
    }

    override fun createBannerView(): IBannerView {
        return HuaweiBannerView()
    }

    override fun createSplashAds(): ISplashAds {
        return HuaweiSplashAds()
    }

    override fun createRewardedAds(): IRewardedAds {
        return HuaweiRewardedAds()
    }
}