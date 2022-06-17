package com.onekeyads.startapp

import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.nativead.NativeAdsContentContainer
import com.startapp.sdk.ads.nativead.NativeAdDetails
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

private const val TAG = "StartIONativeAd"
class StartIONativeAd: INativeAd() {

    private var nativeAd: NativeAdDetails? = null

    override fun loadNativeAd(
        container: ViewGroup,
        contentContainer: NativeAdsContentContainer,
        adId: String,
        nativeAdOption: NativeAdOption,
        callBack: (Boolean) -> Unit
    ) {
        checkMediaSize(contentContainer) { mediaSize, secondaryImageSize ->
            val startAppNativeAd = StartAppNativeAd(container.context)
            val nativeAdPreferences = NativeAdPreferences()
                .setAdsNumber(1)
                .setAutoBitmapDownload(false)
            if (mediaSize != null) {
                nativeAdPreferences.primaryImageSize = mediaSize
            }
            if (secondaryImageSize != null) {
                nativeAdPreferences.secondaryImageSize = secondaryImageSize
            }
            startAppNativeAd.loadAd(nativeAdPreferences, object: AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    val nativeAd = startAppNativeAd.nativeAds.firstOrNull()
                    if (null == nativeAd) {
                        callBack.invoke(false)
                        return
                    }
                    showNativeAd(contentContainer, nativeAd)
                    nativeAd.registerViewForInteraction(container)
                    callBack.invoke(true)
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    Log.i(TAG, "onFailedToReceiveAd")
                    callBack.invoke(false)
                }
            })
        }
    }

    private fun checkMediaSize(contentContainer: NativeAdsContentContainer, callBack: (Int?, Int?) -> Unit) {
        val mediaContent = contentContainer.getChild(NativeAdsContentContainer.ChildType.Media)
        val iconContent = contentContainer.getChild(NativeAdsContentContainer.ChildType.AppIcon)
        if (null == mediaContent) {
            callBack.invoke(null, null)
            return
        }
        mediaContent.post {
            val size = mediaContent.width.coerceAtLeast(mediaContent.height)
            val secondaryImageSize = if (null == iconContent) {
                null
            } else {
                val iconViewSize = iconContent.width.coerceAtLeast(iconContent.height)
                when {
                    iconViewSize <= 72 -> {
                        0
                    }
                    iconViewSize <= 100 -> {
                        1
                    }
                    iconViewSize <= 150 -> {
                        2
                    }
                    else -> {
                        3
                    }
                }
            }
            when {
                size <= 72 -> {
                    callBack.invoke(0, secondaryImageSize)
                }
                size <= 100 -> {
                    callBack.invoke(1, secondaryImageSize)
                }
                size <= 150 -> {
                    callBack.invoke(2, secondaryImageSize)
                }
                size <= 340 -> {
                    callBack.invoke(3, secondaryImageSize)
                }
                else -> {
                    callBack.invoke(4, secondaryImageSize)
                }
            }
        }
    }

    private fun showNativeAd(contentContainer: NativeAdsContentContainer, nativeAdDetails: NativeAdDetails) {
        renderImage(contentContainer, nativeAdDetails)
        contentContainer.getChild(NativeAdsContentContainer.ChildType.HeadLine)?.apply {
            setText(this, nativeAdDetails.title)
        }
        contentContainer.getChild(NativeAdsContentContainer.ChildType.Star)?.apply {
            visibility = View.VISIBLE
            (this as? RatingBar)?.rating = nativeAdDetails.rating
        }
        contentContainer.getChild(NativeAdsContentContainer.ChildType.Price)?.apply {
            setText(this, nativeAdDetails.installs)
        }
        contentContainer.getChild(NativeAdsContentContainer.ChildType.Action)?.apply {
            setText(this, nativeAdDetails.callToAction)
        }
        contentContainer.getChild(NativeAdsContentContainer.ChildType.Description)?.apply {
            setText(this, nativeAdDetails.description)
        }
        contentContainer.getChild(NativeAdsContentContainer.ChildType.Store)?.apply {
            visibility = View.INVISIBLE
        }
        contentContainer.getChild(NativeAdsContentContainer.ChildType.Advertiser)?.apply {
            setText(this, nativeAdDetails.packageName)
        }
    }

    private fun renderImage(contentContainer: NativeAdsContentContainer, nativeAdDetails: NativeAdDetails) {
        nativeAdDetails.loadImages(contentContainer.context) {
            nativeAdDetails.imageBitmap?.let { bitmap ->
                when (val mediaContainer = contentContainer.getChild(NativeAdsContentContainer.ChildType.Media)) {
                    is ViewGroup -> {
                        mediaContainer.addView(
                            ImageView(contentContainer.context).apply {
                                scaleType = ImageView.ScaleType.CENTER_CROP
                                setImageBitmap(bitmap)
                            },
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        )
                    }
                    is ImageView -> {
                        mediaContainer.scaleType = ImageView.ScaleType.CENTER_CROP
                        mediaContainer.setImageBitmap(bitmap)
                    }
                    else -> {
                        mediaContainer?.background = BitmapDrawable(contentContainer.resources, bitmap)
                    }
                }
            }
            nativeAdDetails.secondaryImageBitmap?.let { bitmap ->
                val iconView = contentContainer.getChild(NativeAdsContentContainer.ChildType.AppIcon)
                if (iconView is ImageView) {
                    iconView.setImageBitmap(nativeAdDetails.secondaryImageBitmap)
                } else {
                    iconView?.background = BitmapDrawable(contentContainer.resources, bitmap)
                }
            }
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

    override fun detach(container: ViewGroup) {
        nativeAd?.unregisterView()
    }

}