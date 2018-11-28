package com.nover.flutterfbaudiencenetwork

import android.content.Context
import com.facebook.ads.*
import io.flutter.plugin.common.PluginRegistry.Registrar


class FlutterFBAudienceNetworkPlugin {

  companion object {

    const val bannerViewType = "fb_native_banner_ad_view"

    @JvmStatic
    fun registerWith(registrar: Registrar) {
      AudienceNetworkAds.initialize(registrar.context())

      registrar
          .platformViewRegistry()
          .registerViewFactory(
              bannerViewType,
              FBNativeBannerAdViewFactory(
                  registrar.messenger(),
                  AdLoader(registrar.context())
              )
          )
    }
  }
}

interface AdLoaderListener {

  fun onAdLoaded(ad: Ad)
}

class AdLoader(
    private val context: Context
): NativeAdListener {

  init {
    AudienceNetworkAds.isInAdsProcess(context)
  }

  private val listeners = arrayListOf<AdLoaderListener>()
  private var nativeBannerAd: NativeBannerAd? = null

  private var isLoading = false

  fun addListener(listener: AdLoaderListener) {
    this.listeners.add(listener)
  }

  fun removeListener(listener: AdLoaderListener) {
    this.listeners.remove(listener)
  }

  fun loadAd(placementID: String) {
    if (isLoading) return

    isLoading = true

    if (nativeBannerAd != null && nativeBannerAd!!.isAdLoaded && !nativeBannerAd!!.isAdInvalidated) {
      onAdLoaded(nativeBannerAd!!)
    } else {
      nativeBannerAd = NativeBannerAd(context, placementID)
      nativeBannerAd!!.setAdListener(this)
      nativeBannerAd!!.loadAd()
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
    isLoading = false
  }

  override fun onAdLoaded(ad: Ad) {
    println("nativeBannerAd onAdLoaded")
    isLoading = false

    for (listener in listeners)
      listener.onAdLoaded(ad)
  }

  override fun onLoggingImpression(p0: Ad?) {
    println("nativeBannerAd onLoggingImpression")
  }
}


