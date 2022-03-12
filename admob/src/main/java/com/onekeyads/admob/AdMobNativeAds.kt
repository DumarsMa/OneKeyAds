package com.onekeyads.admob

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.onekeyads.base.view.nativead.INativeAd
import com.onekeyads.base.view.nativead.INativeAdMediaView
import com.onekeyads.base.view.nativead.NativeAdContent

private const val TAG = "AdMobNativeAds"
class AdMobNativeAds: INativeAd() {

    private var nativeAd: NativeAd? = null

    override fun loadNativeAd(
        context: Context,
        adId: String,
        callBack: (Boolean, NativeAdContent?) -> Unit
    ) {
        val adLoader = AdLoader.Builder(context, adId)
            .forNativeAd { nativeAd ->
                this.nativeAd = nativeAd
                val content = nativeAdToContent(context, nativeAd)
                if (null == content) {
                    callBack.invoke(false, null)
                } else {
                    callBack.invoke(true, content)
                }
            }
            .withAdListener(createAdListener(callBack))
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .build()
            )
            .build()
        adLoader.loadAd(
            AdRequest.Builder()
                .build()
        )
    }

    private fun createAdListener(callBack: (Boolean, NativeAdContent?) -> Unit): AdListener {
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
                callBack.invoke(false, null)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.i(TAG, "onAdClosed")
            }
        }
    }

    private fun nativeAdToContent(context: Context, nativeAd: NativeAd): NativeAdContent? {
        val mediaView = if (nativeAd.mediaContent?.hasVideoContent() == true) {
            createVideoView(context, nativeAd.mediaContent!!)
        } else if (nativeAd.images.size > 0) {
            createImageView(context, nativeAd.images[0])
        } else {
             return null
        }
        val nativeContent = NativeAdContent(mediaView)
        return nativeContent
    }

    private fun createImageView(context: Context, image: NativeAd.Image): INativeAdMediaView {
        return object : INativeAdMediaView {

            val imageView = ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageDrawable(drawable)
            }

            override fun isVideo(): Boolean {
                return false
            }

            override fun getView(): View {
                if (null != image.drawable) {
                    imageView.setImageDrawable(image.drawable)
                } else if (null != image.uri) {
                    imageView.setImageURI(image.uri)
                }
                return imageView
            }
        }
    }

    private fun createVideoView(context: Context, mediaContent: MediaContent): INativeAdMediaView {
        return object: INativeAdMediaView {

            private var isLoop: Boolean = false
            private var customLifecycle: INativeAdMediaView.VideoLifecycle? = null
            private val videLifecycle = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoStart() {
                    super.onVideoStart()
                    customLifecycle?.onVideoStart()
                }

                override fun onVideoPause() {
                    super.onVideoPause()
                    customLifecycle?.onVideoPause()
                }

                override fun onVideoPlay() {
                    super.onVideoPlay()
                    customLifecycle?.onVideoPlay()
                }

                override fun onVideoEnd() {
                    super.onVideoEnd()
                    customLifecycle?.onVideoEnd()
                    if (isLoop) {
                        mediaContent.videoController.play()
                    }
                }

                override fun onVideoMute(mute: Boolean) {
                    super.onVideoMute(mute)
                    customLifecycle?.onVideoMute(mute)
                }
            }

            private var mediaView: MediaView = MediaView(context).apply {
                mediaContent.videoController.videoLifecycleCallbacks = videLifecycle
            }

            override fun isVideo(): Boolean {
                return true
            }

            override fun getView(): View {
                mediaView.setMediaContent(mediaContent)
                return mediaView
            }

            override fun setMute(mute: Boolean) {
                mediaContent.videoController.mute(mute)
            }

            override fun setLoop(loop: Boolean) {
                isLoop = loop
            }

            override fun setVideoLifecycle(lifecycle: INativeAdMediaView.VideoLifecycle?) {
                super.setVideoLifecycle(lifecycle)
                customLifecycle = lifecycle
            }
        }
    }

    override fun detach(context: Context) {
        nativeAd?.destroy()
    }
}