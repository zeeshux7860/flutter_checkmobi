import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_checkmobi_platform_interface.dart';

/// An implementation of [FlutterCheckmobiPlatform] that uses method channels.
class MethodChannelFlutterCheckmobi extends FlutterCheckmobiPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_checkmobi');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }


  @override
  Future<String?> reciver() async {
    final version = await methodChannel.invokeMethod<String>('reciver');
    return version;
  }
}
