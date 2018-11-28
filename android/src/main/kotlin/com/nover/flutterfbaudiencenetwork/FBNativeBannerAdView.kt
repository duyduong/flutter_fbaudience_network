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
import kotlin.math.roundToInt

enum class CalMethod {
  /// allow controller to reload the ad
  loadAd
}

class FBNativeBannerAdView(
    context: Context,
    messenger: BinaryMessenger,
    id: Int,
    params: Any?,
    private val adLoader: AdLoader
) : PlatformView, MethodChannel.MethodCallHandler, AdLoaderListener {

  private val view: FBNativeBannerAd = FBNativeBannerAd(context)
  private val methodChannel: MethodChannel = MethodChannel(messenger, "${FlutterFBAudienceNetworkPlugin.bannerViewType}_$id")
  private val placementID: String

  init {
    methodChannel.setMethodCallHandler(this)

    val map = params as HashMap<*, *>

    val contentPadding = Padding.parse(map["contentPadding"] as String)
    view.contentPadding = contentPadding

    val style = map["style"] as HashMap<*, *>
    println("============= style $style")
    view.titleColor = Color.parseColor(style["titleColor"] as String)
    view.socialTextColor = Color.parseColor(style["socialTextColor"] as String)
    view.bgColor = Color.parseColor(style["backgroundColor"] as String)

    placementID = map["placementID"] as String
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
      CalMethod.loadAd -> {
        adLoader.loadAd(placementID)
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

  var titleColor: Int = Color.WHITE
  var socialTextColor: Int = Color.WHITE
  var bgColor: Int = Color.BLACK
  var contentPadding: Padding = Padding.zero

  private val loadingIndicator: ProgressBar
  private val containerView: LinearLayout
  private val adChoicesContainer: RelativeLayout
  private val adTitle: TextView
  private val adSocialContext: TextView
  private val sponsoredLabel: TextView
  private val adCallToAction: Button
  private val adIconView: AdIconView
  private val adLabelView: TextView

  init {
    val inflater = LayoutInflater.from(context)
    inflater.inflate(R.layout.fb_native_banner_ad, this, true)

    loadingIndicator = findViewById(R.id.loading_indicator)
    containerView = findViewById(R.id.native_ad_container)
    adChoicesContainer = findViewById(R.id.ad_choices_container)
    adTitle = findViewById(R.id.native_ad_title)
    adSocialContext = findViewById(R.id.native_ad_social_context)
    sponsoredLabel = findViewById(R.id.native_ad_sponsored_label)
    adCallToAction = findViewById(R.id.native_ad_call_to_action)
    adIconView = findViewById(R.id.native_icon_view)
    adLabelView = findViewById(R.id.ad_attribution)

    adTitle.setTextColor(titleColor)
    adSocialContext.setTextColor(socialTextColor)
    setBackgroundColor(bgColor)
    containerView.setPadding(contentPadding.left.dp(), contentPadding.top.dp(), contentPadding.right.dp(), contentPadding.bottom.dp())

    val color = Color.parseColor("#fbb320")
    adLabelView.background = color.toRoundedColor(3f)
  }

  fun inflateAd(nativeBannerAd: NativeBannerAd) {
    loadingIndicator.visibility = View.GONE

    containerView.visibility = View.VISIBLE
    containerView.setPadding(contentPadding.left, contentPadding.top, contentPadding.right, contentPadding.bottom)

    val adChoicesView = AdChoicesView(context, nativeBannerAd, false)
    adChoicesContainer.addView(adChoicesView)

    adTitle.text = nativeBannerAd.advertiserName

    adSocialContext.text = nativeBannerAd.adSocialContext

    sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

    adCallToAction.text = nativeBannerAd.adCallToAction
    adCallToAction.visibility = if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE

    val clickableViews = arrayListOf<View>()
    clickableViews.add(containerView)
    clickableViews.add(adCallToAction)
    nativeBannerAd.registerViewForInteraction(this, adIconView, clickableViews)
  }
}

data class Padding(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {

  companion object {

    val zero get() = Padding(0,0,0,0)

    fun parse(paddingString: String): Padding {
      val parts = paddingString.split(",")
          .map { it.trim().toFloat().roundToInt() }

      val len = parts.count()

      val left: Int
      val top: Int
      val right: Int
      val bottom: Int

      if (len == 1) {
        val p = parts.first()
        left = p
        top = p
        right = p
        bottom = p
      } else if (len == 2) {
        val hp = parts.first()
        left = hp
        right = hp

        val vp = parts.last()
        top = vp
        bottom = vp
      } else if (len == 3) {
        left = parts[0]
        top = parts[1]
        right = parts[2]
        bottom = 0
      } else {
        left = parts[0]
        top = parts[1]
        right = parts[2]
        bottom = parts[3]
      }

      return Padding(left.dp(), top.dp(), right.dp(), bottom.dp())
    }
  }
}

fun Int.toRoundedColor(radius: Float): Drawable {
  val drawable = GradientDrawable()
  drawable.shape = GradientDrawable.RECTANGLE
  drawable.cornerRadius = radius * Resources.getSystem().displayMetrics.density
  drawable.setColor(this)
  return drawable
}

fun Int.dp(): Int {
  val density = Resources.getSystem().displayMetrics.density
  return (this * density).toInt()
}









