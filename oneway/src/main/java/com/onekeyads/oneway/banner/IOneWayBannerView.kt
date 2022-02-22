package com.onekeyads.oneway.banner

import mobi.oneway.export.feed.IFeedAd

interface IOneWayBannerView {
    fun setAds(ads: List<IFeedAd>)
}