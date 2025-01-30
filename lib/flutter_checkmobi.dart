
import 'flutter_checkmobi_platform_interface.dart';

class FlutterCheckmobi {
  Future<String?> getPlatformVersion() {
    return FlutterCheckmobiPlatform.instance.getPlatformVersion();
  }

  Future<String?> reciver() {
    return FlutterCheckmobiPlatform.instance.reciver();
  }
}
