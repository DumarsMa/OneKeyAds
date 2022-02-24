package com.onekeyads.huawei

import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.banner.IBannerView

private const val TAG = "HuaweiBannerView"
class HuaweiBannerView: IBannerView {

    private var isAttached: Boolean = false
    private var bannerView: BannerView? = null

    override fun attachToBanner(container: FrameLayout, adsId: String, carousel: Boolean) {
        isAttached = true
        bannerView?.apply {
            val parent = this.parent
            if (parent is ViewGroup) {
                parent.removeView(this)
            }
            destroy()
        }
        container.post {
            if (!isAttached) {
                return@post
            }
            bannerView = BannerView(container.context).apply {
                container.addView(this)
                adId = adsId
                bannerAdSize = BannerAdSize(container.width, container.height)
                setBannerRefresh(60)
                adListener = object: AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.i(TAG, "onAdLoaded")
                    }

                    override fun onAdFailed(error: Int) {
                        super.onAdFailed(error)
                        Log.i(TAG, "onAdFailed->$error")
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        Log.i(TAG, "onAdOpened")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Log.i(TAG, "onAdClicked")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        Log.i(TAG, "onAdClosed")
                    }

                    override fun onAdLeave() {
                        super.onAdLeave()
                        Log.i(TAG, "onAdLeave")
                    }
                }
                loadAd(
                    AdParam.Builder()
                        .build()
                )
            }
        }
    }

    override fun detachFromBanner(container: FrameLayout) {
        isAttached = false
    }
}