import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef FBNativeBannerAdCreatedCallback = void Function(NativeAdController controller);

class FBNativeBannerAd extends StatefulWidget {

  final String placementID;
  final FBNativeBannerAdCreatedCallback onCreate;

  FBNativeBannerAd({
    Key key,
    @required this.placementID,
    this.onCreate
  }) : assert(placementID.isNotEmpty, "Placement ID should not be empty"),
    super(key: key);

  @override
  _FBNativeBannerAdState createState() => _FBNativeBannerAdState();
}

class _FBNativeBannerAdState extends State<FBNativeBannerAd> {

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'fb_native_ad_view',
        onPlatformViewCreated: _onPlatformViewCreated,
        creationParamsCodec: StandardMessageCodec(),
        creationParams: {
          "placementID": widget.placementID,
        },
      );
    }

    return Text('$defaultTargetPlatform is not supported PlatformView yet.');
  }

  _onPlatformViewCreated(int id) {
    if (widget.onCreate != null)
      widget.onCreate(NativeAdController._(id));
  }
}


class NativeAdController {

  final MethodChannel _channel;

  NativeAdController._(int id)
      : _channel = new MethodChannel("fb_native_ad_view_$id");

  Future<Null> setTitleColor(String colorString) {
    _channel.invokeMethod("setTitleColor", {"color": colorString});
  }

  Future<Null> setSocialTextColor(String colorString) {
    _channel.invokeMethod("setSocialTextColor", {"color": colorString});
  }

  Future<Null> setBackgroundColor(String colorString) {
    _channel.invokeMethod("setBackgroundColor", {"color": colorString});
  }

  Future<Null> setContentPadding({int top = 0, int left = 0, int bottom = 0, int right = 0}) {
    _channel.invokeMethod("setContentPadding", {
      "top": top,
      "left": left,
      "bottom": bottom,
      "right": right
    });
  }
}

