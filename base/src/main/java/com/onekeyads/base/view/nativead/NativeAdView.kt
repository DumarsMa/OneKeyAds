package com.onekeyads.base.view.nativead

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.R

class NativeAdView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var releaseAfterDetach: Boolean = true
    private var nativeAd: INativeAd? = null
    private var adId: String?
    private var subContainer: NativeAdsContentContainer? = null
    private var videoMute: Boolean
    private var videoLoop: Boolean
    private var choosePosition: Int = 1
    private var mediaAspectRatio: Int = 0
    private var renderDirect = true

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NativeAdView)
        adId = typedArray.getString(R.styleable.NativeAdView_nativeAdId)
        val inflateId = typedArray.getResourceId(R.styleable.NativeAdView_nativeAdInflateId, -1)
        videoMute = typedArray.getBoolean(R.styleable.NativeAdView_nativeAdVideoMute, true)
        videoLoop = typedArray.getBoolean(R.styleable.NativeAdView_nativeAdVideoLoop, false)
        choosePosition = typedArray.getInt(R.styleable.NativeAdView_nativeAdChoicesPlacementPosition, 1)
        mediaAspectRatio = typedArray.getInt(R.styleable.NativeAdView_nativeAdMediaAspectRatio, 0)
        releaseAfterDetach = typedArray.getBoolean(R.styleable.NativeAdView_nativeAdReleaseAfterDetach, true)
        renderDirect = typedArray.getBoolean(R.styleable.NativeAdView_nativeAdRenderDirect, true)
        typedArray.recycle()
        setSubContainer(inflateId)
    }

    fun setSubContainer(inflateId: Int) {
        if (inflateId < 0) {
            return
        }
        val container = LayoutInflater.from(context).inflate(inflateId, this, false)
        if (container !is NativeAdsContentContainer) {
            throw Exception("contentContainer must instance of NativeAdsContentContainer")
        }
        setSubContainer(container)
    }

    fun setSubContainer(container: NativeAdsContentContainer) {
        this.subContainer = container
    }

    fun setReleaseAfterDetach(release: Boolean) {
        releaseAfterDetach = release
    }

    fun setVideoLoop(loop: Boolean) {
        videoLoop = loop
    }

    fun setVideoMute(mute: Boolean) {
        videoMute = mute
    }

    fun setChoosePlacementPosition(position: INativeAd.NativeAdChoosePosition) {
        choosePosition = position.ordinal
    }

    fun setMediaAspectRatio(ratio: INativeAd.MediaAspectRatio) {
        mediaAspectRatio = ratio.ordinal
    }

    fun setAdId(id: String) {
        this.adId = id
    }

    fun load(renderDirect: Boolean = true, callBack: ((Boolean) -> Unit)? = null) {
        if (TextUtils.isEmpty(adId)) {
            throw Exception("must set adId")
        }
        if (null == subContainer) {
            throw Exception("must set subContainer")
        }
        removeAllViews()
        AdsFactory.init(context) { success ->
            if (success) {
                if (null == nativeAd) {
                    nativeAd = AdsFactory.createNativeAd()
                }
                nativeAd?.apply {
                    val nativeAdOption = INativeAd.NativeAdOption().apply {
                        this.renderDirect = renderDirect
                        isVideoMute = videoMute
                        isVideoLoop = videoLoop
                        choosePlacementPosition = when(choosePosition) {
                            0 -> INativeAd.NativeAdChoosePosition.TOP_LEFT
                            1 -> INativeAd.NativeAdChoosePosition.TOP_RIGHT
                            2 -> INativeAd.NativeAdChoosePosition.BOTTOM_RIGHT
                            3 -> INativeAd.NativeAdChoosePosition.BOTTOM_LEFT
                            else -> INativeAd.NativeAdChoosePosition.TOP_RIGHT
                        }
                        mediaAspectRatio = when(this@NativeAdView.mediaAspectRatio) {
                            1 -> INativeAd.MediaAspectRatio.ANY
                            2 -> INativeAd.MediaAspectRatio.LANDSCAPE
                            3 -> INativeAd.MediaAspectRatio.PORTRAIT
                            4 -> INativeAd.MediaAspectRatio.SQUARE
                            else -> INativeAd.MediaAspectRatio.NONE
                        }
                    }
                    loadNativeAd(this@NativeAdView, subContainer!!, adId!!, nativeAdOption) { result ->
                        callBack?.invoke(result)
                    }
                }
            } else {
                callBack?.invoke(false)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!TextUtils.isEmpty(adId) && subContainer != null) {
            load(renderDirect)
        }
    }

    fun release() {
        nativeAd?.detach(this)
    }

    override fun onDetachedFromWindow() {
        if (releaseAfterDetach) {
            nativeAd?.detach(this)
        }
        super.onDetachedFromWindow()
    }
}