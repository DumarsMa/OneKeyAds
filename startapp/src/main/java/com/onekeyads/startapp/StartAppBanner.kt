package com.onekeyads.startapp

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.onekeyads.base.view.banner.IBannerView
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener

private const val TAG = "StartAppBanner"
class StartAppBanner: IBannerView {
    override fun attachToBanner(container: FrameLayout, bannerConfig: IBannerView.BannerConfig) {
        container.addView(
            Banner(container.context, object : BannerListener{
                override fun onReceiveAd(banner: View?) {
                    Log.i(TAG, "onReceiveAd")
                }

                override fun onFailedToReceiveAd(banner: View?) {
                    Log.i(TAG, "onFailedToReceiveAd")
                }

                override fun onImpression(banner: View?) {
                    Log.i(TAG, "onImpression")
                }

                override fun onClick(banner: View?) {
                    Log.i(TAG, "onBannerClick")
                }
            })
        )
    }

    override fun detachFromBanner(container: FrameLayout) {
    }
}