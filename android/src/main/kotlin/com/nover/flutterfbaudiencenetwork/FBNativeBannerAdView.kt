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
import com.facebook.ads.Ad
import com.facebook.ads.AdChoicesView
import com.facebook.ads.AdIconView
import com.facebook.ads.NativeBannerAd
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

enum class CalMethod {
  setTitleColor, setSocialTextColor, setBackgroundColor, setContentPadding
}

class FBNativeBannerAdView(
    context: Context,
    messenger: BinaryMessenger,
    id: Int,
    params: Any?,
    private val adLoader: AdLoader
) : PlatformView, MethodChannel.MethodCallHandler, AdLoaderListener {

  private val view: FBNativeBannerAd = FBNativeBannerAd(context)
  private val methodChannel: MethodChannel = MethodChannel(messenger, "${FlutterFBAudienceNetworkPlugin.viewType}_$id")

  init {
    methodChannel.setMethodCallHandler(this)

    val map = params as HashMap<*, *>
    val placementID = map["placementID"] as String

    adLoader.addListener(this)
    adLoader.loadAd(placementID)
  }

  override fun getView(): View = view

  override fun dispose() {
    adLoader.removeListener(this)
  }

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

  override fun onAdLoaded(ad: Ad) {
    (ad as? NativeBannerAd)?.let {
      view.inflateAd(it)
    }
  }
}

class FBNativeBannerAd @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

  private var titleColor: Int = Color.parseColor("#000000")
  private var socialTextColor: Int = Color.parseColor("#000000")
  private var bgColor: Int = Color.parseColor("#ffffff")

  private var contentPadding: Padding = Padding(0, 0, 0, 0)

  private val loadingIndicator: ProgressBar
    get() = findViewById(R.id.loading_indicator)

  private val containerView: LinearLayout
    get() = findViewById(R.id.native_ad_container)

  private val adChoicesContainer: RelativeLayout
    get() = findViewById(R.id.ad_choices_container)

  private val adTitle: TextView
    get() = findViewById(R.id.native_ad_title)

  private val adSocialContext: TextView
    get() = findViewById(R.id.native_ad_social_context)

  private val sponsoredLabel: TextView
    get() = findViewById(R.id.native_ad_sponsored_label)

  private val adCallToAction: Button
    get() = findViewById(R.id.native_ad_call_to_action)

  private val adIconView: AdIconView
    get() = findViewById(R.id.native_icon_view)

  private val adLabelView: RelativeLayout
    get() = findViewById(R.id.ad_label_view)

  init {
    val inflater = LayoutInflater.from(context)
    inflater.inflate(R.layout.fb_native_banner_ad, this, true)

    setBackgroundColor(bgColor)

    val color = Color.parseColor("#fbb320")
    adLabelView.background = color.toRoundedColor(3f)
  }

  fun inflateAd(nativeBannerAd: NativeBannerAd) {
    setBackgroundColor(bgColor)

    loadingIndicator.visibility = View.GONE

    containerView.visibility = View.VISIBLE
    containerView.setPadding(contentPadding.left, contentPadding.top, contentPadding.right, contentPadding.bottom)

    val adChoicesView = AdChoicesView(context, nativeBannerAd, false)
    adChoicesContainer.addView(adChoicesView)

    adTitle.text = nativeBannerAd.advertiserName
    adTitle.setTextColor(titleColor)

    adSocialContext.text = nativeBannerAd.adSocialContext
    adSocialContext.setTextColor(socialTextColor)

    sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

    adCallToAction.text = nativeBannerAd.adCallToAction
    adCallToAction.visibility = if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE

    val clickableViews = arrayListOf<View>()
    clickableViews.add(containerView)
    clickableViews.add(adCallToAction)
    nativeBannerAd.registerViewForInteraction(this, adIconView, clickableViews)
  }

  fun setTitleColor(call: MethodCall) {
    var colorString = call.argument<String>("color")
    if (colorString.isNullOrEmpty()) colorString = "#000000"

    titleColor = Color.parseColor(colorString)

    adTitle.setTextColor(titleColor)
  }

  fun setSocialTextColor(call: MethodCall) {
    var colorString = call.argument<String>("color")
    if (colorString.isNullOrEmpty()) colorString = "#000000"

    socialTextColor = Color.parseColor(colorString)

    adSocialContext.setTextColor(socialTextColor)
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

    containerView.setPadding(left, top, right, bottom)
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









