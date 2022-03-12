package com.onekeyads.admob

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

    override fun loadNativeAd(
        container: ViewGroup,
        contentContainer: NativeAdsContentContainer,
        adId: String,
        nativeAdOption: NativeAdOption,
        callBack: (Boolean) -> Unit
    ) {
        val adOption = NativeAdOptions.Builder()
            .setAdChoicesPlacement(nativeAdOption.choosePlacementPosition.ordinal)
            .setVideoOptions(VideoOptions.Builder()
                .setStartMuted(nativeAdOption.isVideoMute)
                .build())
            .setMediaAspectRatio(nativeAdOption.mediaAspectRatio.ordinal)
            .build()
        val adLoader = AdLoader.Builder(container.context, adId)
            .forNativeAd { nativeAd ->
                this.nativeAd = nativeAd
                renderNativeAd(nativeAd, container, contentContainer, nativeAdOption)
                callBack.invoke(true)
            }
            .withAdListener(createAdListener(callBack))
            .withNativeAdOptions(
                adOption
            )
            .build()
        adLoader.loadAd(
            AdRequest.Builder()
                .build()
        )
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
                               contentContainer: NativeAdsContentContainer,
                               nativeAdOption: NativeAdOption) {
        (nativeAdView?.parent as? ViewGroup)?.removeView(nativeAdView)
        (contentContainer.parent as? ViewGroup)?.removeView(contentContainer)
        nativeAdView = NativeAdView(container.context).apply {
            addView(contentContainer)
            container.addView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
            nativeAd.mediaContent?.apply {
                if (nativeAdOption.isVideoLoop) {
                    videoController.videoLifecycleCallbacks = object: VideoController.VideoLifecycleCallbacks() {
                        override fun onVideoEnd() {
                            super.onVideoEnd()
                            videoController.play()
                        }
                    }
                }
            }
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
        val mediaView = MediaView(contentContainer.context)
        mediaContainer.addView(mediaView)
        return mediaView
    }

    override fun detach(container: ViewGroup) {
        nativeAd?.destroy()
    }
}