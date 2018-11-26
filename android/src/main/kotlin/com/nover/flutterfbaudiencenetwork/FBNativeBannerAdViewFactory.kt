package com.nover.flutterfbaudiencenetwork

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class FBNativeBannerAdViewFactory(
        private val messenger: BinaryMessenger,
        private val adLoader: AdLoader
): PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    override fun create(context: Context, id: Int, params: Any?): PlatformView {
        return FBNativeBannerAdView(context, messenger, id, params, adLoader)
    }
}