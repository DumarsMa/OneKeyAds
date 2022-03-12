package com.onekeyads.base.view.nativead

import android.view.ViewGroup

abstract class INativeAd {

    abstract fun loadNativeAd(container: ViewGroup,
                              contentContainer: NativeAdsContentContainer,
                              adId: String,
                              nativeAdOption: NativeAdOption,
                              callBack: (Boolean) -> Unit)

    abstract fun detach(container: ViewGroup)

    class NativeAdOption {

        var choosePlacementPosition: NativeAdChoosePosition = NativeAdChoosePosition.TOP_RIGHT

        var mediaAspectRatio: MediaAspectRatio = MediaAspectRatio.NONE

        var isVideoMute: Boolean = false

        var isVideoLoop: Boolean = false
    }

    enum class NativeAdChoosePosition {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    enum class MediaAspectRatio {
        NONE,
        ANY,
        LANDSCAPE,
        PORTRAIT,
        SQUARE
    }
}