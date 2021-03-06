package com.onekeyads.bytedance

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.*
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.banner.IBannerView

private const val TAG = "BannerView"
class BannerView: IBannerView {

    private var container: FrameLayout? = null
    private var bannerAd: TTNativeExpressAd? = null
    override fun attachToBanner(container: FrameLayout, bannerConfig: IBannerView.BannerConfig) {
        val density = container.resources.displayMetrics.density
        TTAdSdk.getAdManager().createAdNative(container.context)
            .loadBannerExpressAd(
                createAdSlot(bannerConfig.adsId, (bannerConfig.width /density).toInt(),
                    (bannerConfig.height / density).toInt()
                ), object: TTAdNative.NativeExpressAdListener {
                    override fun onError(code: Int, msg: String?) {
                        Log.i(TAG, "loadBanner error $code, $msg")
                    }

                    override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                        Log.i(TAG, "onNativeExpressAdLoad ${ads?.size}")
                        val bannerAd = ads?.get(0) ?: return
                        if (bannerConfig.carousel) {
                            bannerAd.setSlideIntervalTime(30 * 1000)
                        }
                        this@BannerView.container = container
                        this@BannerView.bannerAd = bannerAd
                        bannerAd.setExpressInteractionListener(createExpressInteractionListener(container, bannerConfig))
                        bannerAd.setDownloadListener(createDownloadListener())
                        if (container.context is Activity) {
                            bannerAd.setDislikeCallback(container.context as Activity, createDislikeCallBack())
                        }
                        bannerAd.render()
                    }
                })
    }

    private fun createAdSlot(codeId: String,
                             width: Int, height: Int): AdSlot {
        return AdSlot.Builder()
            .setCodeId(codeId)
            .setSupportDeepLink(true)
            .setAdCount(1)
            .setExpressViewAcceptedSize(width.toFloat(), height.toFloat())
            .setAdLoadType(TTAdLoadType.PRELOAD)
            .build()
    }

    private fun createExpressInteractionListener(container: FrameLayout, bannerConfig: IBannerView.BannerConfig): TTNativeExpressAd.ExpressAdInteractionListener {
        return object : TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View?, type: Int) {
                Log.i(TAG, "onAdClicked $type")
            }

            override fun onAdShow(view: View?, type: Int) {
                Log.i(TAG, "onAdShow $type")
            }

            override fun onRenderFail(view: View?, msg: String?, code: Int) {
                Log.i(TAG, "onRenderFail $code $msg")
            }

            override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                Log.i(TAG, "onRenderSuccess $width $height")
                container.removeAllViews()
                container.addView(view, FrameLayout.LayoutParams(bannerConfig.width, bannerConfig.height).apply {
                    gravity = Gravity.CENTER
                })
            }
        }
    }

    private fun createDownloadListener(): TTAppDownloadListener {
        return object : TTAppDownloadListener {
            override fun onIdle() {
                Log.i(TAG, "onIdle")
            }

            override fun onDownloadActive(totalBytes: Long, currentBytes: Long, fileName: String?, appName: String?) {
                Log.i(TAG, "onDownloadActive->appName:$appName, fileName:$fileName")
            }

            override fun onDownloadPaused(totalBytes: Long, currentBytes: Long, fileName: String?, appName: String?) {
                Log.i(TAG, "onDownloadPaused->appName:$appName, fileName:$fileName")
            }

            override fun onDownloadFailed(totalBytes: Long, currentBytes: Long, fileName: String?, appName: String?) {
                Log.i(TAG, "onDownloadFailed->appName:$appName, fileName:$fileName")
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                Log.i(TAG, "onDownloadFinished->appName:$appName, fileName:$fileName")
            }

            override fun onInstalled(fileName: String?, appName: String?) {
                Log.i(TAG, "onInstalled->appName:$appName, fileName:$fileName")
            }
        }
    }

    private fun createDislikeCallBack(): TTAdDislike.DislikeInteractionCallback {
        return object: TTAdDislike.DislikeInteractionCallback {
            override fun onShow() {
                Log.i(TAG, "onDislikeShow")
            }

            override fun onSelected(position: Int, value: String?, enforce: Boolean) {
                Log.i(TAG, "onDislikeSelected $position $value $enforce")
                container?.visibility = View.GONE
            }

            override fun onCancel() {
                Log.i(TAG, "onDislikeCancel")
            }
        }
    }

    override fun detachFromBanner(container: FrameLayout) {
        bannerAd?.destroy()
        this.container = null
    }
}