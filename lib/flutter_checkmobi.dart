import 'flutter_checkmobi_platform_interface.dart';

class FlutterCheckmobi {
  Future<String?> getPlatformVersion() {
    return FlutterCheckmobiPlatform.instance.getPlatformVersion();
  }

  Future<String?> reciver() {
    return FlutterCheckmobiPlatform.instance.reciver();
  }

  Future<String?> chechUser() {
    return FlutterCheckmobiPlatform.instance.chechUser();
  }

  Future<String?> setApiKey(String key) {
    return FlutterCheckmobiPlatform.instance.setApiKey(key);
  }

  Future<void> listenForData({
    required Function(String val) onError,
    required Function(String val) onnVerifyPin,
    required Function(String val) onComplete,
    required Function(String val) onData,
  }) {
    return FlutterCheckmobiPlatform.instance.listenForData(
        onComplete: onComplete,
        onData: onData,
        onError: onError,
        onnVerifyPin: onnVerifyPin);
  }

  Future<String?> createMissedCall(
      {required String countryCode, required String phoneNumber}) {
    return FlutterCheckmobiPlatform.instance
        .createMissedCall(countryCode: countryCode, phoneNumber: phoneNumber);
  }
}
