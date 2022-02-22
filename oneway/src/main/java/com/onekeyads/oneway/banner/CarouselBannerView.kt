package com.onekeyads.oneway.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import mobi.oneway.export.feed.IFeedAd

class CarouselBannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs), IOneWayBannerView {

    private val adapter = Adapter()
    init {
        setAdapter(adapter)
    }

    override fun setAds(ads: List<IFeedAd>) {
        adapter.updateAds(ads)
    }

    private class Adapter : PagerAdapter() {
        private val ads = mutableListOf<IFeedAd>()
        private val views = mutableMapOf<Int, SingleBannerView>()

        fun updateAds(ads: List<IFeedAd>) {
            this.ads.clear()
            this.ads.addAll(ads)
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return ads.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun instantiateItem(container: ViewGroup, position: Int): SingleBannerView {
            var item = views[position]
            if (null == item) {
                item = SingleBannerView(container.context).apply {
                    if (ads.size > position) {
                        setAd(ads[position])
                    }
                }
                views[position] = item
            }
            container.addView(item)
            return item
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            if (obj is SingleBannerView) {
                container.removeView(obj)
            } else {
                val item = views[position] ?: return
                container.removeView(item)
            }
        }
    }
}