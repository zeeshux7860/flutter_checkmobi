import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_checkmobi/flutter_checkmobi.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        // initialRoute: '/',
        routes: {
          // '/': (context) => HomePage(),
          '/verification_screen': (context) => VerificationScreen(),
        },
        home: HomePage());
  }
}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String _platformVersion = 'Unknown';
  final dataRetrive = FlutterCheckmobi();
  // DataReceiver dataRetrive = DataReceiver();
  @override
  void initState() {
    dataRetrive.listenForData(
      onComplete: (val) {
        print(val);
      },
      onData: (val) {},
      onError: (val) {
        print(val);
      },
      onnVerifyPin: (val) {},
    );
    super.initState();
    // initPlatformState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(onPressed: () {
        dataRetrive.setApiKey("3205B0AC-A58E-47CE-8912-4F70F7C82938");
      }),
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Column(
        children: [
          TextButton(
              onPressed: () async {
                try {
                  var res = await dataRetrive.createMissedCall(
                      countryCode: "91", phoneNumber: "7003113356");
                  print(res);
                } catch (e) {
                  print(e.toString());
                }
              },
              child: Text("data")),
          Center(
            child: Text('Running on: $_platformVersion\n'),
          ),
        ],
      ),
    );
  }
}

// class DataReceiver {
//   static const MethodChannel _channel = MethodChannel('flutter_checkmobi');

//   // Method to listen for data from Java
//   Future<void> listenForData() async {
//     _channel.setMethodCallHandler((MethodCall call) async {
//       if (call.method == 'receiveData') {
//         // Handle data received from Java
//         String data = call.arguments;
//         print("Data received from Java: $data");
//         // Do something with the data
//       }
//       if (call.method == 'error') {
//         // Handle data received from Java
//         String data = call.arguments;
//         print("Data received from Java: $data");
//         // Do something with the data
//       }
//       if (call.method == 'sendIdORPin') {
//         // Handle data received from Java
//         String data = call.arguments;
//         print("Data received from Java: $data");
//         // Do something with the data
//       }
//       if (call.method == 'verifiedUser') {
//         // Handle data received from Java
//         String data = call.arguments;
//         print("Data received from Java: $data");
//         // Do something with the data
//       }
//     });
//   }

//   // Optional: Method to call Java method
//   Future<void> sendDataToJava(String data) async {
//     try {
//       await _channel.invokeMethod('sendData', {'data': data});
//     } on PlatformException {
//       rethrow;
//     }
//   }

//   // Optional: Method to call Java method
//   Future<void> createMissedCall(
//       {required String countryCode, required String phoneNumber}) async {
//     try {
//       await _channel.invokeMethod('callIInitialize',
//           {'countryCode': countryCode, 'phoneNumber': phoneNumber});
//     } on PlatformException catch (e) {
//       rethrow;
//     }
//   }
// }

class VerificationScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          leading: BackButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pop(context);
            },
          ),
          title: Text("Checkmobi Verification")),
      body: Center(
        child: Text("Flutter UI is now rendering inside CheckmobiBaseActivity"),
      ),
    );
  }
}
