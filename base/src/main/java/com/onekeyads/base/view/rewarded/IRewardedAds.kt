package com.onekeyads.base.view.rewarded

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.onekeyads.base.AdsFactory
import java.lang.ref.WeakReference

private const val TAG = "IRewardedAds"
abstract class IRewardedAds {

    enum class RewardedResult {
        FAIL,
        REWARDED,
        CLOSE
    }

    private val activityCallBacks = object: Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity == currentActivity?.get()) {
                detach(activity)
            }
        }
    }
    private var currentActivity: WeakReference<Activity>? = null
    fun attach(context: Activity, adsId: String, callBack: (RewardedResult) -> Unit) {
        if (TextUtils.isEmpty(adsId)) {
            Log.i(TAG, "attach adsId empty")
            callBack.invoke(RewardedResult.FAIL)
            return
        }
        AdsFactory.init(context) { success ->
            Log.i(TAG, "init $success")
            if (!success) {
                callBack.invoke(RewardedResult.FAIL)
                return@init
            }
            currentActivity = WeakReference(context)
            context.application.unregisterActivityLifecycleCallbacks(activityCallBacks)
            context.application.registerActivityLifecycleCallbacks(activityCallBacks)
            loadRewardedAds(context, adsId, callBack)
        }
    }

    protected abstract fun loadRewardedAds(context: Activity, adsId: String, callBack: (RewardedResult) -> Unit)

    open fun detach(context: Activity) {
        context.application.unregisterActivityLifecycleCallbacks(activityCallBacks)
        currentActivity = null
    }
}