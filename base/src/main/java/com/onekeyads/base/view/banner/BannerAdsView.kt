package com.onekeyads.base.view.banner

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.R

private const val TAG = "BannerAdsView"
class BannerAdsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs){

    private var carousel: Boolean = true
    private var adsId: String = ""
    private var viewImpl: IBannerView? = null
    private var aspectRatio: Float? = null

    init {
        val typedValue = context.obtainStyledAttributes(attrs, R.styleable.BannerAdsView)
        adsId = typedValue.getString(R.styleable.BannerAdsView_adsId) ?: ""
        carousel = typedValue.getBoolean(R.styleable.BannerAdsView_adsCarousel, true)
        getAspectRatio(typedValue)
        typedValue.recycle()
        loadAds(adsId)
    }

    private fun getAspectRatio(typedValue: TypedArray) {
        val stringRatio = typedValue.getString(R.styleable.BannerAdsView_aspectRatio)
        if (TextUtils.isEmpty(stringRatio)) {
            return
        }
        val ratioArray = stringRatio!!.split(":")
        if (ratioArray.size != 2) {
            throw Exception("aspectRatio $stringRatio format error, correct format eg:4:3")
        }
        val wRatio = ratioArray[0].toIntOrNull()
        if (wRatio == null || wRatio <= 0) {
            throw Exception("aspectRatio $stringRatio format error, correct format eg:4:3")
        }
        val hRatio = ratioArray[1].toIntOrNull()
        if (hRatio == null || hRatio <= 0) {
            throw Exception("aspectRatio $stringRatio format error, correct format eg:4:3")
        }
        aspectRatio = wRatio.toFloat() / hRatio
    }

    fun loadAds(adsId: String) {
        if (!TextUtils.equals(adsId, this.adsId)) {
            viewImpl?.detachFromBanner(this)
        }
        this.adsId = adsId
        if (!TextUtils.isEmpty(adsId)) {
            AdsFactory.init(context) { success ->
                Log.i(TAG, "init result $success")
                if (!success) {
                    return@init
                }
                post {
                    viewImpl = AdsFactory.createBannerView()
                    val aspectRatio = this.aspectRatio
                    val containerWidth = width
                    val containerHeight = height
                    if (null == aspectRatio || containerHeight == 0 || containerWidth == 0) {
                        viewImpl?.attachToBanner(this,
                            IBannerView.BannerConfig(adsId, carousel, containerWidth, containerHeight))
                    } else if (containerWidth.toFloat() / containerHeight > aspectRatio) {
                        viewImpl?.attachToBanner(this,
                            IBannerView.BannerConfig(adsId, carousel,
                                (containerHeight * aspectRatio).toInt(), containerHeight))
                    } else {
                        viewImpl?.attachToBanner(this,
                            IBannerView.BannerConfig(adsId, carousel,
                                containerWidth, (containerWidth / aspectRatio).toInt()))
                    }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        viewImpl?.detachFromBanner(this)
        super.onDetachedFromWindow()
    }
}