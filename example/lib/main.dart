import 'package:flutter/material.dart';
import 'package:flutter_fbaudience_network/flutter_fbaudience_network.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  static const placementID = "YOUR_PLACEMENT_ID";

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          height: 100.0,
          child: FBNativeBannerAd(
            placementID: placementID,
            onCreate: (NativeAdController controller) {
              controller.setTitleColor("#ffffff");
              controller.setSocialTextColor("#ffffff");
              controller.setBackgroundColor("#232325");
              controller.setContentPadding(top: 10, left: 10, right: 10);
            },
          ),
        ),
      ),
    );
  }
}
