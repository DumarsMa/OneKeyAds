package com.onekeyads.unity

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

private const val TAG = "UnityBannerView"
class UnityBannerView: IBannerView, BannerView.IListener {
    private var runnable: Runnable? = null
    private var adsView: BannerView? = null

    override fun attachToBanner(container: FrameLayout, bannerConfig: IBannerView.BannerConfig) {
        container.post {
            val activity = container.context ?: return@post
            if (activity !is Activity) {
                return@post
            }
            Log.i(TAG, "loadBanner")
            val adsView = BannerView(activity as? Activity, bannerConfig.adsId, UnityBannerSize(container.width,
                container.height))
            adsView.listener = this
            adsView.load()
            this.adsView = adsView
            container.addView(adsView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER
                }
            )
        }
    }

    override fun onBannerLoaded(view: BannerView?) {
        Log.i(TAG, "onBannerLoaded")
    }

    override fun onBannerClick(view: BannerView?) {
        Log.i(TAG, "onBannerClick")
    }

    override fun onBannerFailedToLoad(view: BannerView?, error: BannerErrorInfo?) {
        Log.i(TAG, "onBannerFailedToLoad->${error?.errorCode}, ${error?.errorMessage}")
    }

    override fun onBannerLeftApplication(view: BannerView?) {
        Log.i(TAG, "onBannerLeftApplication")
    }

    override fun detachFromBanner(container: FrameLayout) {
        container.removeCallbacks(runnable)
    }
}