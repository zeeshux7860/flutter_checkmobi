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
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> reciver() async {
    final version = await methodChannel.invokeMethod<String>('reciver');
    return version;
  }

  @override
  Future<String?> createMissedCall(
      {required String countryCode, required String phoneNumber}) async {
    return await methodChannel.invokeMethod('callIInitialize',
        {'countryCode': countryCode, 'phoneNumber': phoneNumber});
  }

  // Method to listen for data from Java
  @override
  Future<void> listenForData({
    required Function(String val) onError,
    required Function(String val) onnVerifyPin,
    required Function(String val) onComplete,
    required Function(String val) onData,
  }) async {
    methodChannel.setMethodCallHandler((MethodCall call) async {
      if (call.method == 'receiveData') {
        // Handle data received from Java
        String data = call.arguments;
        onData(data);
        // Do something with the data
      }
      if (call.method == 'error') {
        // Handle data received from Java
        String data = call.arguments;
        onError(data);
        // Do something with the data
      }
      if (call.method == 'sendIdORPin') {
        // Handle data received from Java
        String data = call.arguments;
        onnVerifyPin(data);

        // Do something with the data
      }
      if (call.method == 'verifiedUser') {
        // Handle data received from Java
        String data = call.arguments;
        onComplete(data);
        // Do something with the data
      }
    });
  }

  @override
  Future<String?> setApiKey(String key) async {
    return await methodChannel.invokeMethod('setApiKey', {'api_key': key});
  }

  @override
  Future<String?> chechUser() async {
    return await methodChannel.invokeMethod('checkUser');
  }
}
