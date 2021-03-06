package com.onekeyads.base.view.splash

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
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
            if (activity == currentActivity) {
                onResume(activity, currentCallback)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity == currentActivity) {
                onPause(activity, currentCallback)
            }
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

    final fun attach(activity: Activity, savedInstanceState: Bundle?, splashAdsId: String, callBack: (Boolean) -> Unit) {
        AdsFactory.init(activity.applicationContext) { success ->
            if (!success) {
                callBack.invoke(false)
            } else {
                currentActivity = activity
                activity.application.unregisterActivityLifecycleCallbacks(activityCallBacks)
                activity.application.registerActivityLifecycleCallbacks(activityCallBacks)
                currentCallback = callBack
                loadSplash(activity, savedInstanceState, splashAdsId, callBack)
            }
        }
    }

    protected abstract fun loadSplash(activity: Activity, savedInstanceState: Bundle?, splashAdsId: String, callBack: (Boolean) -> Unit)

    open fun onStart(activity: Activity, callBack: ((Boolean) -> Unit)? = null) {
    }

    open fun onResume(activity: Activity, callBack: ((Boolean) -> Unit)? = null) {
    }

    open fun onPause(activity: Activity, callBack: ((Boolean) -> Unit)? = null) {
    }

    open fun onStop(activity: Activity, callBack: ((Boolean) -> Unit)? = null) {
    }

    protected fun contextValid(): Boolean {
        val activity = currentActivity ?: return false
        return when {
            activity.isFinishing -> {
                false
            }
            else -> !activity.isDestroyed
        }
    }

    open fun detach(activity: Activity) {
        activity.application.unregisterActivityLifecycleCallbacks(activityCallBacks)
        currentActivity = null
        currentCallback = null
    }
}