package com.onekeyads.base.view.splash

import android.app.Activity

/**
 * 开屏广告
 */
interface ISplashAds {

    fun attach(activity: Activity, splashAdsId: String, callBack: (Boolean) -> Unit)

    fun onResume(activity: Activity) {
    }

    fun onStop(activity: Activity) {
    }

    fun detach(activity: Activity)
}