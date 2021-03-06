package com.onekeyads.admob

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.nativead.NativeAdsContentContainer

private const val TAG = "AdMobNativeAds"
class AdMobNativeAds: INativeAd() {

    private var nativeAd: NativeAd? = null
    private var nativeAdView: NativeAdView? = null
    private var adLoader: AdLoader? = null
    private var nativeAdOption: NativeAdOption? = null

    override fun loadNativeAd(
        container: ViewGroup,
        contentContainer: NativeAdsContentContainer,
        adId: String,
        nativeAdOption: NativeAdOption,
        callBack: (Boolean) -> Unit
    ) {
        Log.i(TAG, "load ad start ${nativeAdOption.renderDirect}")
        if (null != nativeAd) {
            Log.i(TAG, "load ad hasLoad")
            if (nativeAdOption.renderDirect) {
                renderNativeAd(nativeAd!!, container, contentContainer)
            }
            callBack.invoke(true)
            return
        }
        this.nativeAdOption = nativeAdOption
        if (adLoader?.isLoading == true) {
            Log.i(TAG, "load ad loading")
            return
        }
        val adOption = NativeAdOptions.Builder()
            .setAdChoicesPlacement(nativeAdOption.choosePlacementPosition.ordinal)
            .setVideoOptions(VideoOptions.Builder()
                .setStartMuted(nativeAdOption.isVideoMute)
                .build())
            .setMediaAspectRatio(nativeAdOption.mediaAspectRatio.ordinal)
            .build()
        adLoader = AdLoader.Builder(container.context, adId)
            .forNativeAd { nativeAd ->
                if (container.context is Activity && !isActivityValid(container.context as Activity)) {
                    return@forNativeAd
                }
                this.nativeAd = nativeAd
                if (this.nativeAdOption?.renderDirect != false) {
                    renderNativeAd(nativeAd, container, contentContainer)
                }
                callBack.invoke(true)
            }
            .withAdListener(createAdListener(callBack))
            .withNativeAdOptions(
                adOption
            )
            .build().apply {
                loadAd(
                    AdRequest.Builder()
                        .build()
                )
            }
    }

    private fun createAdListener(callBack: (Boolean) -> Unit): AdListener {
        return object: AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.i(TAG, "onAdClicked")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.i(TAG, "onAdLoaded")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.i(TAG, "onAdOpened")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                Log.i(TAG, "onAdFailedToLoad $error")
                callBack.invoke(false)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.i(TAG, "onAdClosed")
            }
        }
    }

    private fun renderNativeAd(nativeAd: NativeAd,
                               container: ViewGroup,
                               contentContainer: NativeAdsContentContainer) {
        (nativeAdView?.parent as? ViewGroup)?.removeView(nativeAdView)
        (contentContainer.parent as? ViewGroup)?.removeView(contentContainer)
        nativeAdView = NativeAdView(container.context).apply {
            addView(contentContainer)
            container.addView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
            mediaView = addMediaView(contentContainer)
            headlineView = contentContainer.getChild(NativeAdsContentContainer.ChildType.HeadLine)?.apply {
                (this as? TextView)?.text = nativeAd.headline
            }
            starRatingView = contentContainer.getChild(NativeAdsContentContainer.ChildType.Star)?.apply {
                if (null == nativeAd.starRating) {
                    visibility = View.INVISIBLE
                } else {
                    visibility = View.VISIBLE
                    (this as? RatingBar)?.rating = nativeAd.starRating!!.toFloat()
                }
            }
            priceView = contentContainer.getChild(NativeAdsContentContainer.ChildType.Price)?.apply {
                setText(this, nativeAd.price)
            }
            callToActionView = contentContainer.getChild(NativeAdsContentContainer.ChildType.Action)?.apply {
                setText(this, nativeAd.callToAction)
            }
            bodyView = contentContainer.getChild(NativeAdsContentContainer.ChildType.Description)?.apply {
                setText(this, nativeAd.body)
            }
            storeView = contentContainer.getChild(NativeAdsContentContainer.ChildType.Store)?.apply {
                setText(this, nativeAd.store)
            }
            iconView = contentContainer.getChild(NativeAdsContentContainer.ChildType.AppIcon)?.apply {
                visibility = if (null == nativeAd.icon?.drawable) {
                    View.INVISIBLE
                } else {
                    (this as? ImageView)?.setImageDrawable(nativeAd.icon!!.drawable)
                    View.VISIBLE
                }
            }
            advertiserView = contentContainer.getChild(NativeAdsContentContainer.ChildType.Advertiser)?.apply {
                setText(this, nativeAd.advertiser)
            }
            setNativeAd(nativeAd)
        }
    }

    private fun setText(view: View, text: String?) {
        if (TextUtils.isEmpty(text)) {
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
            (view as? TextView)?.text = text
        }
    }

    private fun addMediaView(contentContainer: NativeAdsContentContainer): MediaView {
        val mediaContainer = contentContainer.getChild(NativeAdsContentContainer.ChildType.Media) as? ViewGroup
            ?: throw Exception("not found Media Container, you must set a ViewGroup as media container")
        mediaContainer.removeAllViews()
        val mediaView = MediaView(contentContainer.context)
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        mediaContainer.addView(mediaView)
        return mediaView
    }

    override fun detach(container: ViewGroup) {
        nativeAd?.destroy()
        nativeAd = null
    }

    private fun isActivityValid(activity: Activity): Boolean {
        return when {
            activity.isFinishing -> {
                false
            }
            else -> !activity.isDestroyed
        }
    }
}