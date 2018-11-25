package com.nover.flutterfbaudiencenetwork

import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterFBAudienceNetworkPlugin {

  companion object {

    const val viewType = "fb_native_ad_view"

    @JvmStatic
    fun registerWith(registrar: Registrar) {
      registrar.platformViewRegistry().registerViewFactory(viewType, FBNativeAdViewFactory(registrar.messenger()))
    }
  }
}




