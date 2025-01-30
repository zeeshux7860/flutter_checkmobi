import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_checkmobi_method_channel.dart';

abstract class FlutterCheckmobiPlatform extends PlatformInterface {
  /// Constructs a FlutterCheckmobiPlatform.
  FlutterCheckmobiPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterCheckmobiPlatform _instance = MethodChannelFlutterCheckmobi();

  /// The default instance of [FlutterCheckmobiPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterCheckmobi].
  static FlutterCheckmobiPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterCheckmobiPlatform] when
  /// they register themselves.
  static set instance(FlutterCheckmobiPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> reciver() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
