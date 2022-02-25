package com.onekeyads.oneway

import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import com.onekeyads.base.view.banner.IBannerView
import com.onekeyads.oneway.banner.CarouselBannerView
import com.onekeyads.oneway.banner.SingleBannerView
import mobi.oneway.export.Ad.OWFeedAd
import mobi.oneway.export.AdListener.feed.OWFeedAdListener
import mobi.oneway.export.enums.OnewaySdkError
import mobi.oneway.export.feed.IFeedAd

private const val TAG = "OnewayBannerView"
class OnewayBannerView: IBannerView {

    private var feedAd: OWFeedAd? = null
    private var isAttached: Boolean = false

    override fun attachToBanner(container: FrameLayout, bannerConfig: IBannerView.BannerConfig) {
        isAttached = true
        feedAd = OWFeedAd(container.context, bannerConfig.adsId).apply {
            load(object: OWFeedAdListener {
                override fun onError(error: OnewaySdkError?, msg: String?) {
                    Log.i(TAG, "loadBanner error->$error, $msg")
                }

                override fun onAdLoad(ads: MutableList<IFeedAd>?) {
                    Log.i(TAG, "onAdLoad ${ads?.size}")
                    if (ads?.isEmpty() != false) {
                        return
                    }
                    val view = if (bannerConfig.carousel) {
                        CarouselBannerView(container.context)
                    } else {
                        SingleBannerView(container.context)
                    }
                    container.addView(view, FrameLayout.LayoutParams(bannerConfig.width,
                        bannerConfig.height).apply {
                        gravity = Gravity.CENTER
                    })
                    view.setAds(ads)
                }
            })
        }
    }

    override fun detachFromBanner(container: FrameLayout) {
        isAttached = false
    }
}