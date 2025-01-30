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
  String _platformVersion = 'Unknown';
  final _flutterCheckmobiPlugin = FlutterCheckmobi();
DataReceiver dataRetrive = DataReceiver();
  @override
  void initState() {
    dataRetrive.listenForData();
    super.initState();
    // initPlatformState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        floatingActionButton: FloatingActionButton(onPressed: (){
dataRetrive.sendDataToJava("data");

        }),
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            TextButton(onPressed: ()async{
                await _flutterCheckmobiPlugin.reciver() ;
            }, child: Text("data")),
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
          ],
        ),
      ),
    );
  }
}



class DataReceiver {
  static const MethodChannel _channel = MethodChannel('flutter_checkmobi');

  // Method to listen for data from Java
  Future<void> listenForData() async {
    _channel.setMethodCallHandler((MethodCall call) async {
      if (call.method == 'receiveData') {
        // Handle data received from Java
        String data = call.arguments;
        print("Data received from Java: $data");
        // Do something with the data
      }
    });
  }

  // Optional: Method to call Java method
  Future<void> sendDataToJava(String data) async {
    try {
      await _channel.invokeMethod('sendData', {'data': data});
    } on PlatformException catch (e) {
      print("Failed to send data to Java: ${e.message}");
    }
  }
}
