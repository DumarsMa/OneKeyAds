package com.onekeyads.base.view.splash

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.onekeyads.base.AdsFactory

/**
 * 开屏广告
 */
abstract class ISplashAds {

    private val activityCallBacks = object: Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
            if (activity == currentActivity) {
                onStart(activity, currentCallback)
            }
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            if (activity == currentActivity) {
                onStop(activity, currentCallback)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity == currentActivity) {
                detach(activity)
            }
        }
    }
    private var currentActivity: Activity? = null
    private var currentCallback: ((Boolean) -> Unit)? = null

    final fun attach(activity: Activity, splashAdsId: String, callBack: (Boolean) -> Unit) {
        AdsFactory.init(activity.applicationContext) { success ->
            if (!success) {
                callBack.invoke(false)
            } else {
                activity.application.registerActivityLifecycleCallbacks(activityCallBacks)
                currentCallback = callBack
                loadSplash(activity, splashAdsId, callBack)
            }
        }
    }

    protected abstract fun loadSplash(activity: Activity, splashAdsId: String, callBack: (Boolean) -> Unit)

    open fun onStart(activity: Activity, callBack: ((Boolean) -> Unit)? = null) {
    }

    open fun onStop(activity: Activity, callBack: ((Boolean) -> Unit)? = null) {
    }

    open fun detach(activity: Activity) {
        activity.application.unregisterActivityLifecycleCallbacks(activityCallBacks)
        currentActivity = null
        currentCallback = null
    }
}