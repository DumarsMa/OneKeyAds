package com.onekeyads.base.view.banner

import android.view.ViewGroup
import android.widget.FrameLayout

interface IBannerView {

    fun attachToBanner(container: FrameLayout, bannerConfig: BannerConfig)

    fun detachFromBanner(container: FrameLayout)

    data class BannerConfig(val adsId: String, val carousel: Boolean,
                            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
                            val height: Int = ViewGroup.LayoutParams.MATCH_PARENT)
}