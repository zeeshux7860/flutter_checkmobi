Okay! Here’s how you can create the documentation for your `flutter_checkmobi` plugin based on the code you provided.

---

# flutter_checkmobi Plugin Documentation

`flutter_checkmobi` is a Flutter plugin for integrating Checkmobi SDK for phone number verification in your Flutter application. This plugin provides a method to start the phone number verification process using Checkmobi's verification service.


## Support the Project

If you like this project and would like to support it, your support means a lot!

## Buy Me a Coffee ☕️

You can support this project by clicking the button below to make a donation. Thank you for your support! 🙏

[![Buy Me a Coffee](https://raw.githubusercontent.com/appcraftconsulting/buymeacoffee/master/Images/snapshot-bmc-button.png)](https://www.buymeacoffee.com/zeeshux)


## Installation

1. Add the plugin to your `pubspec.yaml` file:

```yaml
dependencies:
  flutter:
    sdk: flutter
  flutter_checkmobi:

```

2. Run `flutter pub get` to install the plugin.

## Platform Support

- Android

## Usage

To use the plugin, follow these steps:

### 1. Set API Key
Before using any feature of the plugin, you need to set the API key provided by Checkmobi.

```dart
import 'package:flutter_checkmobi/flutter_checkmobi.dart';

void setApiKey(String apiKey) {
  FlutterCheckmobi.setApiKey(apiKey);
}
```

### 2. Initialize and Start Verification
To initialize and start the phone number verification process, call the `callIInitialize` method with the required parameters:

```dart
import 'package:flutter_checkmobi/flutter_checkmobi.dart';

void startVerification(String phoneNumber, String countryCode) {
  FlutterCheckmobi.callIInitialize(
    phoneNumber: phoneNumber,
    countryCode: countryCode,
  ).then((result) {
    print('Verification process started: $result');
  }).catchError((error) {
    print('Error: $error');
  });
}
```

### 3. Check User Status
To check whether the user has been verified, use the `checkUser` method. It returns the phone number and server ID if verified.

```dart
import 'package:flutter_checkmobi/flutter_checkmobi.dart';

void checkUser() {
  FlutterCheckmobi.checkUser().then((result) {
    print('Verified User: $result');
  }).catchError((error) {
    print('Error: $error');
  });
}
```

## Methods

### `setApiKey`
Sets the API key for the Checkmobi SDK.

**Arguments:**
- `api_key` (String) - The API key provided by Checkmobi.

**Usage:**
```dart
FlutterCheckmobi.setApiKey('your-api-key');
```

### `callIInitialize`
Starts the phone number verification process.

**Arguments:**
- `phoneNumber` (String) - The phone number to verify.
- `countryCode` (String) - The country code of the phone number.

**Returns:** 
- A result indicating whether the verification process started successfully.

### `checkUser`
Checks whether the user has been verified.

**Returns:**
- A JSON string containing the phone number and server ID if the user is verified.


###  Listening for Data
You can listen for data, including any errors or verification events, using the `listenForData` method.

```dart
dataRetrive.listenForData(
  onComplete: (val) {
    print(val); // Log the verification result.
  },
  onData: (val) {},
  onError: (val) {
    print(val); // Handle error
    Fluttertoast.showToast(msg: val); // Display error message
  },
  onnVerifyPin: (val) {},
);
```

## Errors

- `401`: API key not set.
- `404`: Account not found.
- `409`: An error occurred during the user verification process.



### Key Methods in the Example:

- **`setApiKey`**: Sets the API key for Checkmobi.
- **`createMissedCall`**: Starts the phone number verification process via missed call.
- **`listenForData`**: Listens for events like verification completion or errors.

## Error Handling

- If the API key is not set correctly, errors will be shown in the console.
- Any verification errors are caught and displayed using `Fluttertoast.showToast`.

---

### Key Changes in the Code:

1. **Navigation to `VerificationScreen`:**
   - After calling the `createMissedCall` method to verify the phone number, the app navigates to the `/verification_screen` using `Navigator.pushNamed(context, '/verification_screen')`.

2. **Verification Screen UI (`VerificationScreen`):**
   - This screen is displayed when the user starts the verification process. It shows a loading indicator (`CircularProgressIndicator`) and a message indicating that verification is in progress.

3. **Back Button Action:**
   - The back button on the `VerificationScreen` will exit the app using `SystemNavigator.pop()`.

---

### What Happens:

1. User enters the phone number and country code.
2. When the user presses "Verify Now", the app starts the verification process using `createMissedCall`.
3. The app then navigates to the `VerificationScreen`, where a loading indicator is shown while the verification process is ongoing.
4. If the user presses the back button, the app will exit.

---


This documentation will guide users through the steps needed to set up and utilize the `flutter_checkmobi` plugin for phone number verification in their Flutter applications.


## Support the Project

(Again) If you like this project and would like to support it, your support means a lot!

## Buy Me a Coffee ☕️

You can support this project by clicking the button below to make a donation. Thank you for your support! 🙏

[![Buy Me a Coffee](https://raw.githubusercontent.com/appcraftconsulting/buymeacoffee/master/Images/snapshot-bmc-button.png)](https://www.buymeacoffee.com/zeeshux)