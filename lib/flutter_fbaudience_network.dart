import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef FBNativeBannerAdCreatedCallback = void Function(NativeAdController controller);

const bannerViewType = "fb_native_banner_ad_view";

class FBNativeBannerAd extends StatefulWidget {

  final String placementID;
  final EdgeInsets padding;
  final EdgeInsets margin;
  final AdStyle style;
  final FBNativeBannerAdCreatedCallback onCreate;

  FBNativeBannerAd({
    Key key,
    @required this.placementID,
    this.padding = EdgeInsets.zero,
    this.margin = EdgeInsets.zero,
    this.style = const AdStyle(),
    this.onCreate
  }) : assert(placementID.isNotEmpty, "Placement ID should not be empty"),
    super(key: key);

  @override
  _FBNativeBannerAdState createState() => _FBNativeBannerAdState();
}

class _FBNativeBannerAdState extends State<FBNativeBannerAd> {

  @override
  Widget build(BuildContext context) {
    final height = 70.0 + (widget.padding.top + widget.padding.bottom);

    if (defaultTargetPlatform == TargetPlatform.android) {
      final contentPadding = "${widget.padding.left},${widget.padding.top},${widget.padding.right},${widget.padding.bottom}";

      return Container(
        margin: widget.margin,
        height: height,
        child: AndroidView(
          viewType: bannerViewType,
          onPlatformViewCreated: _onPlatformViewCreated,
          creationParamsCodec: StandardMessageCodec(),
          creationParams: {
            "placementID": widget.placementID,
            "style": widget.style.toParams(),
            "contentPadding": contentPadding,
          },
        ),
      );
    }

    return Container(
      padding: widget.padding,
      margin: widget.margin,
      height: height,
      color: widget.style.backgroundColor,
      child: Center(
        child: Text(
          '$defaultTargetPlatform is not supported PlatformView yet.',
          style: TextStyle(color: widget.style.titleColor),
        ),
      ),
    );
  }

  _onPlatformViewCreated(int id) {
    if (widget.onCreate != null)
      widget.onCreate(NativeAdController._(id));
  }
}

class AdStyle {

  final Color titleColor;
  final Color socialTextColor;
  final Color backgroundColor;

  const AdStyle({
    this.titleColor = Colors.white,
    this.socialTextColor = Colors.white,
    this.backgroundColor = const Color(0xff232325)
  });

  dynamic toParams() {
    return {
      "titleColor": _colorToHEX(titleColor),
      "socialTextColor": _colorToHEX(socialTextColor),
      "backgroundColor": _colorToHEX(backgroundColor),
    };
  }

  _colorToHEX(Color color) {
    return "#${color.value.toRadixString(16).padLeft(8, '0')}";
  }
}

class NativeAdController {

  final MethodChannel _channel;

  NativeAdController._(int id) : _channel = new MethodChannel("${bannerViewType}_$id");

  Future<Null> loadAd() async {
    await _channel.invokeMethod("loadAd", null);
    return null;
  }
}

