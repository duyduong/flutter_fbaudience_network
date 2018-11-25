package com.nover.flutterfbaudiencenetwork

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.facebook.ads.*
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

enum class CalMethod {
    setTitleColor, setSocialTextColor, setBackgroundColor, setContentPadding
}

class FBNativeAdView(
        private val context: Context,
        private val messenger: BinaryMessenger,
        private val id: Int,
        private val params: Any?
): PlatformView, MethodChannel.MethodCallHandler, NativeAdListener {

    private val view: FBNativeBannerAd = FBNativeBannerAd(context)
    private val methodChannel: MethodChannel

    private var nativeBannerAd: NativeBannerAd

    init {
        val map = params as HashMap<*, *>
        val placementID = map["placementID"] as String

        nativeBannerAd = NativeBannerAd(context, placementID)
        nativeBannerAd.setAdListener(this)
        nativeBannerAd.loadAd()

        methodChannel = MethodChannel(messenger, "${FlutterFBAudienceNetworkPlugin.viewType}_$id")
        methodChannel.setMethodCallHandler(this)
    }

    override fun getView(): View = view

    override fun dispose() { }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val method = CalMethod.valueOf(call.method)
        when (method) {
            CalMethod.setTitleColor -> {
                view.setTitleColor(call)
                result.success(null)
            }
            CalMethod.setSocialTextColor -> {
                view.setSocialTextColor(call)
                result.success(null)
            }
            CalMethod.setBackgroundColor -> {
                view.setBackgroundColor(call)
                result.success(null)
            }
            CalMethod.setContentPadding -> {
                view.setContentPadding(call)
                result.success(null)
            }
        }
    }

    override fun onAdClicked(p0: Ad?) {
        println("nativeBannerAd onAdClicked")
    }

    override fun onMediaDownloaded(ad: Ad?) {
        println("nativeBannerAd onMediaDownloaded")
    }

    override fun onError(ad: Ad?, error: AdError) {
        println("nativeBannerAd onError ${error.errorMessage}")
    }

    override fun onAdLoaded(ad: Ad) {
        println("nativeBannerAd onAdLoaded")
        if (nativeBannerAd != ad) return
        view.inflateAd(nativeBannerAd)
    }

    override fun onLoggingImpression(p0: Ad?) {
        println("nativeBannerAd onLoggingImpression")
    }
}

class FBNativeBannerAd @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var titleColor: Int = Color.parseColor("#000000")
    private var socialTextColor: Int = Color.parseColor("#000000")
    private var bgColor: Int = Color.parseColor("#ffffff")

    private var contentPadding: Padding = Padding(0,0,0,0)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.fb_native_banner_ad, this, true)

        val adLabelView = findViewById<RelativeLayout>(R.id.ad_label_view)
        val color = Color.parseColor("#fbb320")
        adLabelView.background = color.toRoundedColor(3f)
    }

    fun inflateAd(nativeBannerAd: NativeBannerAd) {
        nativeBannerAd.unregisterView()

        setBackgroundColor(bgColor)

        val loadingIndicator = findViewById<ProgressBar>(R.id.loading_indicator)
        loadingIndicator.visibility = View.GONE

        val layout = findViewById<LinearLayout>(R.id.native_ad_container)
        layout.visibility = View.VISIBLE
        layout.setPadding(contentPadding.left, contentPadding.top, contentPadding.right, contentPadding.bottom)

        val adChoicesView = AdChoicesView(context, nativeBannerAd, false)
        findViewById<RelativeLayout>(R.id.ad_choices_container).addView(adChoicesView)
        nativeBannerAd.adChoicesIcon

        val nativeAdTitle = findViewById<TextView>(R.id.native_ad_title)
        nativeAdTitle.text = nativeBannerAd.advertiserName
        nativeAdTitle.setTextColor(titleColor)

        val nativeAdSocialContext = findViewById<TextView>(R.id.native_ad_social_context)
        nativeAdSocialContext.text = nativeBannerAd.adSocialContext
        nativeAdSocialContext.setTextColor(socialTextColor)

        val sponsoredLabel = findViewById<TextView>(R.id.native_ad_sponsored_label)
        sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

        val nativeAdCallToAction = findViewById<Button>(R.id.native_ad_call_to_action)
        nativeAdCallToAction.text = nativeBannerAd.adCallToAction
        nativeAdCallToAction.visibility = if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE

        val nativeAdIconView = findViewById<AdIconView>(R.id.native_icon_view)
        val clickableViews = arrayListOf<View>()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)
        nativeBannerAd.registerViewForInteraction(this, nativeAdIconView, clickableViews)
    }

    fun setTitleColor(call: MethodCall) {
        var colorString = call.argument<String>("color")
        if (colorString.isNullOrEmpty()) colorString = "#000000"

        titleColor = Color.parseColor(colorString)

        val nativeAdTitle = findViewById<TextView>(R.id.native_ad_title)
        if (nativeAdTitle != null)
            nativeAdTitle.setTextColor(titleColor)
    }

    fun setSocialTextColor(call: MethodCall) {
        var colorString = call.argument<String>("color")
        if (colorString.isNullOrEmpty()) colorString = "#000000"

        socialTextColor = Color.parseColor(colorString)

        val nativeAdSocialContext = findViewById<TextView>(R.id.native_ad_social_context)
        if (nativeAdSocialContext != null)
            nativeAdSocialContext.setTextColor(socialTextColor)
    }

    fun setBackgroundColor(call: MethodCall) {
        var colorString = call.argument<String>("color")
        if (colorString.isNullOrEmpty()) colorString = "#ffffff"

        bgColor = Color.parseColor(colorString)
        setBackgroundColor(bgColor)
    }

    fun setContentPadding(call: MethodCall) {
        val density = Resources.getSystem().displayMetrics.density
        val top = (call.argument<Int>("top")!! * density).toInt()
        val left = (call.argument<Int>("left")!! * density).toInt()
        val bottom = (call.argument<Int>("bottom")!! * density).toInt()
        val right = (call.argument<Int>("right")!! * density).toInt()

        contentPadding = Padding(left, top, right, bottom)

        findViewById<LinearLayout>(R.id.native_ad_container)?.setPadding(left, top, right, bottom)
    }
}

data class Padding(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
)

fun Int.toRoundedColor(radius: Float): Drawable {
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.RECTANGLE
    drawable.cornerRadius = radius * Resources.getSystem().displayMetrics.density
    drawable.setColor(this)
    return drawable
}









