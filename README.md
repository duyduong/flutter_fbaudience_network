[![pub package](https://img.shields.io/pub/v/flutter_fbaudience_network.svg)](https://pub.dartlang.org/packages/flutter_fbaudience_network) 


# flutter_fbaudience_network

Plugin to integrate Facebook Native Ad to Flutter application

***Warning:***
The plugin is based on Flutter `PlatformView` (`AndroidView`) to create a custom widget from Native View. Therefore, only Android is supported at the moment.
For iOS, wait for Flutter team to implement `iOSView` equivalent.

## Getting Started

For help getting started with Flutter, view our online [documentation](http://flutter.io/).

## How it works

The plugin provides `FBNativeBannerAd` widget, that can add to anywhere in Flutter application.

### Integrate banner view widget

`FBNativeBannerAd` is a standalone widget. Its height is fixed plus padding, so you don't need to worry about the height

```dart
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
            placementID: placementID, // required params from Facebook
            padding: EdgeInsets.all(8.0), // content padding apply for native view
            margin: EdgeInsets.only(bottom: 20.0),
            style: AdStyle( // using AdStyle to change some views
              titleColor: Colors.white,
              socialTextColor: Colors.blueGrey,
              backgroundColor: Colors.black,
            ),
            onCreate: (NativeAdController controller) {
              // controller.loadAd(); // Keep controller, then you can re-load the add any time
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
````

`FBNativeBannerAd` widget contains some properties:
- `placementID`: required placement ID from Facebook. If you want to run test ads, please use `"YOUR_PLACEMENT_ID"`
- `style`: use this to change title, social text and background color 

## Features Request

As the plugin is not completed yet (Android only), so it will be actively updated

Feel free to email me about the features you want to include in the plugin, I am happy to discuss.
 
