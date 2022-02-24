package com.onekeyads.app

import android.app.Application
import com.onekeyads.base.AdsFactory

class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AdsFactory.setConfig(
            AdsConfig(this).apply {
                debuggable = true
            }
        )
    }
}