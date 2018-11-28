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
        body: ListView(
          children: <Widget>[
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
            FBNativeBannerAd(
              placementID: placementID,
              padding: EdgeInsets.all(8.0),
              margin: EdgeInsets.only(bottom: 20.0),
              style: AdStyle(
                titleColor: Colors.white,
                socialTextColor: Colors.blueGrey,
                backgroundColor: Colors.black,
              ),
              onCreate: (NativeAdController controller) {
                // controller.loadAd();
              },
            ),
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
            Container(
              height: 150.0,
              color: Colors.green,
              margin: EdgeInsets.only(bottom: 20.0),
            ),
          ],
        ),
      ),
    );
  }
}
