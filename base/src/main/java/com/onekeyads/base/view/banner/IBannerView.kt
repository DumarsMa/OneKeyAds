package com.onekeyads.base.view.banner

import android.widget.FrameLayout

interface IBannerView {
    fun attachToBanner(container: FrameLayout,
                       adsId: String,
                       carousel: Boolean)

    fun detachFromBanner(container: FrameLayout)
}