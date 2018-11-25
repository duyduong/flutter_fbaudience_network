#import "FlutterFbaudienceNetworkPlugin.h"
#import <flutter_fbaudience_network/flutter_fbaudience_network-Swift.h>

@implementation FlutterFbaudienceNetworkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterFbaudienceNetworkPlugin registerWithRegistrar:registrar];
}
@end
