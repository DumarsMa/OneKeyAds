package com.onekeyads.base.view.nativead

import android.content.Context
import com.onekeyads.base.AdsFactory

abstract class INativeAd {

    fun createNativeAd(context: Context, adId: String, callBack: (Boolean, NativeAdContent?) -> Unit) {
        AdsFactory.init(context.applicationContext) { success ->
            if (success) {
                loadNativeAd(context, adId, callBack)
            } else {
                callBack.invoke(false, null)
            }
        }
    }

    abstract fun loadNativeAd(context: Context, adId: String, callBack: (Boolean, NativeAdContent?) -> Unit)

    abstract fun detach(context: Context)
}