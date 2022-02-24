package com.onekeyads.factory

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.onekeyads.admob.AdMobBanner
import com.onekeyads.admob.AdMobRewardAds
import com.onekeyads.admob.AdMobSplashAds
import com.onekeyads.base.IAdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.base.view.rewarded.IRewardedAds
import com.onekeyads.base.view.splash.ISplashAds

private const val TAG = "AdsFactoryImpl"
class AdsFactoryImpl: IAdsFactory {

    private var hasInit = false
    override fun init(context: Context, callback: (Boolean) -> Unit) {
        if (hasInit) {
            callback.invoke(true)
            return
        }
        MobileAds.initialize(context.applicationContext, object: OnInitializationCompleteListener {
            override fun onInitializationComplete(status: InitializationStatus) {
//                if (status.adapterStatusMap.isEmpty()) {
//                    Log.i(TAG, "onInitializationComplete fail adapterStatusMap empty")
//                    hasInit = false
//                    callback.invoke(false)
//                    return
//                }
//                status.adapterStatusMap.forEach { entry ->
//                    if (entry.value.initializationState == AdapterStatus.State.NOT_READY) {
//                        hasInit = false
//                        callback.invoke(false)
//                        Log.i(TAG, "onInitializationComplete state not ready ${entry.value.description}")
//                        return
//                    }
//                }
                Log.i(TAG, "onInitializationComplete success")
                hasInit = true
                callback.invoke(true)
            }
        })
    }

    override fun createBannerView(): IBannerView {
        return AdMobBanner()
    }

    override fun createSplashAds(): ISplashAds {
        return AdMobSplashAds()
    }

    override fun createRewardedAds(): IRewardedAds {
        return AdMobRewardAds()
    }
}