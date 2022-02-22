package com.onekeyads.base.view.banner

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.FrameLayout
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.R

class BannerAdsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs){

    private var carousel: Boolean = true
    private var adsId: String = ""
    private var viewImpl: IBannerView? = null

    init {
        val typedValue = context.obtainStyledAttributes(attrs, R.styleable.BannerAdsView)
        adsId = typedValue.getString(R.styleable.BannerAdsView_adsId) ?: ""
        carousel = typedValue.getBoolean(R.styleable.BannerAdsView_adsCarousel, true)
        typedValue.recycle()
        loadAds(adsId)
    }

    fun loadAds(adsId: String) {
        if (!TextUtils.equals(adsId, this.adsId)) {
            viewImpl?.detachFromBanner(this)
        }
        this.adsId = adsId
        if (!TextUtils.isEmpty(adsId)) {
            viewImpl = AdsFactory.createBannerView()
            viewImpl?.attachToBanner(this, adsId, carousel)
        }
    }

    override fun onDetachedFromWindow() {
        viewImpl?.detachFromBanner(this)
        super.onDetachedFromWindow()
    }
}