package com.onekeyads.base.view.nativead

import android.view.View

interface INativeAdMediaView {

    fun getView(): View

    fun isVideo(): Boolean

    /**
     * 视频静音
     */
    fun setMute(mute: Boolean) {
    }

    /**
     * 视频是否循环播放
     */
    fun setLoop(loop: Boolean) {
    }

    /**
     * 播放视频
     */
    fun playVideo() {
    }

    fun setVideoLifecycle(lifecycle: VideoLifecycle?) {}

    interface VideoLifecycle {

        fun onVideoEnd() {
        }

        fun onVideoMute(mute: Boolean) {
        }

        fun onVideoPause() {
        }

        fun onVideoPlay() {
        }

        fun onVideoStart() {}
    }
}