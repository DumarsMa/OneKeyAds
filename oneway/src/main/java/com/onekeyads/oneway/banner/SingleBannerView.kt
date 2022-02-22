package com.onekeyads.oneway.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.onekeyads.oneway.R
import mobi.oneway.export.AdListener.feed.OWFeedAdEventListener
import mobi.oneway.export.feed.IFeedAd

private const val TAG = "SingleBannerView"
class SingleBannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), IOneWayBannerView {

    private var bgView: ImageView? = null
    private var iconView: ImageView? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_oneway_banner_single, this, true)
        bgView = findViewById(R.id.oneway_singleads_image)
        iconView = findViewById(R.id.oneway_singleads_icon)
    }

    override fun setAds(ads: List<IFeedAd>) {
        if (ads.isEmpty()) {
            return
        }
        setAd(ads[0])
    }

    fun setAd(ad: IFeedAd) {
        iconView?.let { view ->
            Glide.with(view)
                .load(ad.iconImage)
                .into(view)
        }
        bgView?.let { view ->
            if (ad.images?.isEmpty() != false) {
                return@let
            }
            Glide.with(view)
                .load(ad.images[0])
                .into(view)
        }
        ad.handleAdEvent(this, object: OWFeedAdEventListener {
            override fun onExposured(ad: IFeedAd?) {
                Log.i(TAG, "onExposured")
            }

            override fun onClicked(ad: IFeedAd?) {
                Log.i(TAG, "onClicked")
            }
        })
    }

}