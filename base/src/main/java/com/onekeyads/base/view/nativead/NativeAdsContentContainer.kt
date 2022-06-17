package com.onekeyads.base.view.nativead

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import com.onekeyads.base.R

class NativeAdsContentContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs){

    private val childrenId = mutableMapOf<ChildType, Int>()
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NativeAdsContentContainer)
        childrenId[ChildType.Action] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdActionId, -1)
        childrenId[ChildType.Media] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdMediaId, -1)
        childrenId[ChildType.HeadLine] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdHeadLineId, -1)
        childrenId[ChildType.Star] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdStarId, -1)
        childrenId[ChildType.Store] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdStoreId, -1)
        childrenId[ChildType.Description] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdDescId, -1)
        childrenId[ChildType.Price] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdPriceId, -1)
        childrenId[ChildType.Advertiser] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdAdvertiserId, -1)
        childrenId[ChildType.AppIcon] = typedArray.getResourceId(R.styleable.NativeAdsContentContainer_nativeAdAppIconId, -1)
        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    fun getChild(childType: ChildType): View? {
        val childId = childrenId[childType]
        if (null != childId && childId > 0) {
            return findViewById(childId)
        }
        return children.find { view ->
            (view.layoutParams as? LayoutParams)?.childType == childType.ordinal
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return when (lp) {
            is LayoutParams -> {
                LayoutParams(lp)
            }
            is FrameLayout.LayoutParams -> {
                LayoutParams(lp)
            }
            is MarginLayoutParams -> {
                LayoutParams(lp)
            }
            else -> {
                LayoutParams(lp)
            }
        }
    }

    class LayoutParams: FrameLayout.LayoutParams {

        var childType: Int? =  null
        constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
            val typedAttr = context.obtainStyledAttributes(attrs, R.styleable.NativeAdsView_Layout)
            childType = typedAttr.getInt(R.styleable.NativeAdsView_Layout_nativeAdChildType, -1)
            typedAttr.recycle()
        }

        constructor(width: Int, height: Int): super(width, height)

        constructor(width: Int, height: Int, gravity: Int): super(width, height, gravity)

        constructor(source: FrameLayout.LayoutParams): super(source)

        constructor(source: ViewGroup.LayoutParams): super(source)

        constructor(source: MarginLayoutParams): super(source)

        constructor(source: LayoutParams): super(source) {
            this.childType = source.childType
        }
    }

    enum class ChildType {
        Media, //视频或图片容器
        HeadLine, //标题
        Star, //评分
        Description,//描述
        Price,//价格
        Store, //应用市场
        Action, //跳转或安装按钮
        AppIcon, //应用图标
        Advertiser, //应用发布商
    }
}