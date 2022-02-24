package com.onekeyads.unity

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.FrameLayout
import com.onekeyads.base.AdsFactory
import com.onekeyads.base.view.banner.IBannerView
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

private const val TAG = "UnityBannerView"
class UnityBannerView: IBannerView, BannerView.IListener {

    private var container: FrameLayout? = null
    private var unitId: String? = null

    private var runnable: Runnable? = null
    private var adsView: BannerView? = null

    override fun attachToBanner(container: FrameLayout, config: String, carousel: Boolean) {
        this.container = container
        this.unitId = config
        checkSdkState()
    }

    private fun checkSdkState(delay: Long = 0) {
        val runnable = Runnable {
            val context = container?.context ?: return@Runnable
            AdsFactory.init(context) { success ->
                if (success) {
                    Log.i(TAG, "checkSdkState sdk init success")
                    loadBanner()
                } else {
                    Log.i(TAG, "checkSdkState sdk init fail")
                    checkSdkState(5)
                }
            }
        }
        this.runnable = runnable
        if (delay == 0L) {
            runnable.run()
        } else {
            container?.postDelayed(runnable, delay)
        }
    }

    private fun loadBanner() {
        if (TextUtils.isEmpty(unitId)) {
            return
        }
        container?.post {
            val container = this.container
            val activity = container?.context ?: return@post
            if (activity !is Activity) {
                return@post
            }
            Log.i(TAG, "loadBanner")
            val adsView = BannerView(activity as? Activity, unitId, UnityBannerSize(container.width,
                container.height))
            adsView.listener = this
            adsView.load()
            this.adsView = adsView
            container.addView(adsView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun onBannerLoaded(view: BannerView?) {
        Log.i(TAG, "onBannerLoaded")
    }

    override fun onBannerClick(view: BannerView?) {
        Log.i(TAG, "onBannerClick")
    }

    override fun onBannerFailedToLoad(view: BannerView?, error: BannerErrorInfo?) {
        Log.i(TAG, "onBannerFailedToLoad->${error?.errorCode}, ${error?.errorMessage}")
    }

    override fun onBannerLeftApplication(view: BannerView?) {
        Log.i(TAG, "onBannerLeftApplication")
    }

    override fun detachFromBanner(container: FrameLayout) {
        container.removeCallbacks(runnable)
        this.container = null
    }
}