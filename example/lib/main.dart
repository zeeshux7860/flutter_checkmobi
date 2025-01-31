import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_checkmobi/flutter_checkmobi.dart';
import 'package:fluttertoast/fluttertoast.dart';

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
        Fluttertoast.showToast(
          msg: val,
        );
      },
      onnVerifyPin: (val) {},
    );
    super.initState();
    // initPlatformState();
  }

  final TextEditingController phoneController = TextEditingController();
  final TextEditingController countryCodeController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(onPressed: () {}),
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Column(
        children: [
          TextButton(
              onPressed: () async {
                try {
                  var isSaved = await dataRetrive
                      .setApiKey("3205B0AC-A58E-47CE-8912-4F70F7C82938");
                  print(isSaved);
                } catch (e) {
                  print(e.toString());
                }
              },
              child: Text("Set Api Key")),
          TextField(
            controller: countryCodeController,
            decoration: InputDecoration(
              labelText: 'Enter Country Code',
              hintText: 'e.g., 91',
            ),
            keyboardType: TextInputType.number,
          ),
          SizedBox(height: 16),
          TextField(
            controller: phoneController,
            decoration: InputDecoration(
              labelText: 'Enter Phone Number',
              hintText: 'e.g., 1234567890',
            ),
            keyboardType: TextInputType.phone,
          ),
          TextButton(
              onPressed: () async {
                try {
                  var res = await dataRetrive.createMissedCall(
                      countryCode: countryCodeController.text,
                      phoneNumber: phoneController.text);
                  print(res);
                } catch (e) {
                  print(e.toString());
                }
              },
              child: Text("Verify Now")),
        ],
      ),
    );
  }
}

class VerificationScreen extends StatelessWidget {
  const VerificationScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          leading: BackButton(
            onPressed: () {
              SystemNavigator.pop();
              // Navigator.pop(context);
            },
          ),
          title: Text("Checkmobi Verification")),
      body: Center(
        child: Text("Flutter UI is now rendering inside CheckmobiBaseActivity"),
      ),
    );
  }
}
