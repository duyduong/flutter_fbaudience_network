package com.nover.flutterfbaudiencenetwork

import android.content.Context
import com.facebook.ads.AudienceNetworkAds
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class FBNativeAdViewFactory(
        private val messenger: BinaryMessenger
): PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    override fun create(context: Context, id: Int, params: Any?): PlatformView {
        AudienceNetworkAds.initialize(context)
        return FBNativeAdView(context, messenger, id, params)
    }
}