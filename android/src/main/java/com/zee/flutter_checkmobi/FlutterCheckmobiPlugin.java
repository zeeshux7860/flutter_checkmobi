package com.zee.flutter_checkmobi;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** FlutterCheckmobiPlugin */
public class FlutterCheckmobiPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware,  PluginRegistry.ActivityResultListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Activity activity;
  private static MethodChannel channel;
  private static String mApiKey = "";
//  private static final String CHECKMOBI_SECRET_KEY = "";

  private static final String SHARED_PREFS_FILE = "android_checkmobi_sample_prefs";
  private static final String LAST_USED_API_KEY = "last_used_api_key";

  private static final int VERIFICATION_RC = 1;
  private MethodChannel.Result pendingResult;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_checkmobi");
    channel.setMethodCallHandler(this);
    // Wait until activity is available




  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
//    Log.d("CALLINGTESTING", "onMethodCall: "+call.method);
    if (call.method.equals("callIInitialize")) {
      if (Objects.equals(mApiKey, "")) {
        sendError("Please set api key");
        result.error("401", "Please set api key", "Make sure your api key is valid");
        return;
      }
      setupCheckmobi();
      saveLastUsedApiKey();
      String id = call.argument("id");
      String phoneNumner = call.argument("phoneNumber");
      String countryCode = call.argument("countryCode");
      pendingResult = result;
      startVerificationProcess(phoneNumner,countryCode,id);
//      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }
    else
    if (call.method.equals("setApiKey")) {
      mApiKey= call.argument("api_key");
      result.success("update Api key");
    }
    else
    if (call.method.equals("checkUser")) {
        try {
            checkuser(result);
        } catch (JSONException e) {
          result.error("409",e.getMessage(),"account not found");
            throw new RuntimeException(e);
        }

    }
    else {


      result.notImplemented();
    }
  }
  static {
    System.loadLibrary("native-lib");
  }

  // Send data to Flutter (called from any Java class)
  public static void sendDataToFlutter(String data) {
    if (channel != null) {
      channel.invokeMethod("receiveData", data);
    }
  }

  // Send data to Flutter (called from any Java class)
  public static void sendIdORPin(String data) {
    if (channel != null) {
      channel.invokeMethod("sendIdORPin", data);
    }
  }

  // Send data to Flutter (called from any Java class)
  public static void sendError(String data) {
    if (channel != null) {
      channel.invokeMethod("error", data);
    }
  }

  // Send data to Flutter (called from any Java class)
  public static void verifiedUser(String data) {
    if (channel != null) {
      channel.invokeMethod("verifiedUser", data);
    }
  }
  // Method to start verification process
  private void startVerificationProcess( String phoneNumber,
                                         String countryCode,
                                         String id) {

    if (activity == null) {
      Log.e("FlutterCheckmobiPlugin", "Activity is null, cannot start verification.");
      return;
    }
    checkIfNumberAlreadyVerified();
    activity.
    startActivityForResult(
            CheckmobiSdk.getInstance()
                    .createVerificationIntentBuilder()
                    .build(activity,
                            phoneNumber,countryCode,id
                            ),
            VERIFICATION_RC);
//checkIfNumberAlreadyVerified();
    pendingResult.success("Verification process started!");
  }
//  @Overridepackage com.zee.flutter_checkmobi;
//
//import static android.app.Activity.RESULT_OK;
//import static android.content.Context.MODE_PRIVATE;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Objects;
//
//import io.flutter.embedding.engine.plugins.FlutterPlugin;
//import io.flutter.embedding.engine.plugins.activity.ActivityAware;
//import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
//import io.flutter.plugin.common.MethodCall;
//import io.flutter.plugin.common.MethodChannel;
//import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
//import io.flutter.plugin.common.MethodChannel.Result;
//import io.flutter.plugin.common.PluginRegistry;
//
///** FlutterCheckmobiPlugin */
//public class FlutterCheckmobiPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware,  PluginRegistry.ActivityResultListener {
//  /// The MethodChannel that will the communication between Flutter and native Android
//  ///
//  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
//  /// when the Flutter Engine is detached from the Activity
//  private Activity activity;
//  private static MethodChannel channel;
//  private static String mApiKey = "";
////  private static final String CHECKMOBI_SECRET_KEY = "";
//
//  private static final String SHARED_PREFS_FILE = "android_checkmobi_sample_prefs";
//  private static final String LAST_USED_API_KEY = "last_used_api_key";
//
//  private static final int VERIFICATION_RC = 1;
//  private MethodChannel.Result pendingResult;
//  @Override
//  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
//
//    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_checkmobi");
//    channel.setMethodCallHandler(this);
//    // Wait until activity is available
//
//
//
//
//  }
//
//  @Override
//  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
////    Log.d("CALLINGTESTING", "onMethodCall: "+call.method);
//    if (call.method.equals("callIInitialize")) {
//      if (Objects.equals(mApiKey, "")) {
//        sendError("Please set api key");
//        result.error("401", "Please set api key", "Make sure your api key is valid");
//        return;
//      }
//      setupCheckmobi();
//      saveLastUsedApiKey();
//      String id = call.argument("id");
//      String phoneNumner = call.argument("phoneNumber");
//      String countryCode = call.argument("countryCode");
//      pendingResult = result;
//      startVerificationProcess(phoneNumner,countryCode,id);
////      result.success("Android " + android.os.Build.VERSION.RELEASE);
//    }
//    else
//    if (call.method.equals("setApiKey")) {
//      mApiKey= call.argument("api_key");
//      result.success("update Api key");
//    }
//    else
//    if (call.method.equals("checkUser")) {
//        try {
//            checkuser(result);
//        } catch (JSONException e) {
//          result.error("409",e.getMessage(),"account not found");
//            throw new RuntimeException(e);
//        }
//
//    }
//    else {
//
//
//      result.notImplemented();
//    }
//  }
//  static {
//    System.loadLibrary("native-lib");
//  }
//
//  // Send data to Flutter (called from any Java class)
//  public static void sendDataToFlutter(String data) {
//    if (channel != null) {
//      channel.invokeMethod("receiveData", data);
//    }
//  }
//
//  // Send data to Flutter (called from any Java class)
//  public static void sendIdORPin(String data) {
//    if (channel != null) {
//      channel.invokeMethod("sendIdORPin", data);
//    }
//  }
//
//  // Send data to Flutter (called from any Java class)
//  public static void sendError(String data) {
//    if (channel != null) {
//      channel.invokeMethod("error", data);
//    }
//  }
//
//  // Send data to Flutter (called from any Java class)
//  public static void verifiedUser(String data) {
//    if (channel != null) {
//      channel.invokeMethod("verifiedUser", data);
//    }
//  }
//  // Method to start verification process
//  private void startVerificationProcess( String phoneNumber,
//                                         String countryCode,
//                                         String id) {
//
//    if (activity == null) {
//      Log.e("FlutterCheckmobiPlugin", "Activity is null, cannot start verification.");
//      return;
//    }
//    checkIfNumberAlreadyVerified();
//    activity.
//    startActivityForResult(
//            CheckmobiSdk.getInstance()
//                    .createVerificationIntentBuilder()
//                    .build(activity,
//                            phoneNumber,countryCode,id
//                            ),
//            VERIFICATION_RC);
////checkIfNumberAlreadyVerified();
//    pendingResult.success("Verification process started!");
//  }
////  @Override
////  protected void onResume() {
////    super.onResume();
////    checkIfNumberAlreadyVerified();
////  }
//  private void checkIfNumberAlreadyVerified() {
//    String verifiedNumber = CheckmobiSdk.getInstance().getVerifiedNumber(activity);
//    String serveerId = CheckmobiSdk.getInstance().getVerifiedNumberServerId(activity);
//    if (verifiedNumber != null) {
//      JSONObject data = new JSONObject();
//      try {
//        data.put("phoneNumber",verifiedNumber);
//        data.put("requestId",serveerId);
//        String encodedJson = data.toString();
//        verifiedUser(encodedJson);
//      } catch (JSONException e) {
//        throw new RuntimeException(e);
//      }
////            startActivity(new Intent(this, SuccessActivity.class));
//      Log.d("DEBUG", "checkIfNumberAlreadyVerified: success" +verifiedNumber );
//      activity.
//      finish();
//    }else {
//      Log.d("FAILED", "checkIfNumberAlreadyVerified: failed");
//    }
//  }
//
//  private void checkuser(Result result) throws JSONException {
//    String verifiedNumber = CheckmobiSdk.getInstance().getVerifiedNumber(activity);
//    String serveerId = CheckmobiSdk.getInstance().getVerifiedNumberServerId(activity);
//    if (verifiedNumber != null) {
//      JSONObject data = new JSONObject();
//
//        data.put("phoneNumber",verifiedNumber);
//        data.put("requestId",serveerId);
//        String encodedJson = data.toString();
//
//
//  activity.
//              finish();
//      result.success(encodedJson);
////      return   verifiedUser(encodedJson);
//    }else{
//      sendError("Account not found");
//      result.error("404","Account not found","no user found");
//    }
//  }
//
//  @Override
//  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
//    channel.setMethodCallHandler(null);
//  }
//  private void setupCheckmobi() {
//    CheckmobiSdk.getInstance().setApiKey(mApiKey);
//  }
//  private void retrieveLastUsedApiKey() {
//
//    mApiKey =
//
//            activity.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE).getString(LAST_USED_API_KEY, null);
//  }
//
//  private void saveLastUsedApiKey() {
//    if (activity == null) {
//      Log.e("FlutterCheckmobiPlugin", "Activity is null, cannot start verification.");
//      return;
//    }
//   activity. getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE)
//            .edit()
//            .putString(LAST_USED_API_KEY, mApiKey)
//            .apply();
//  }
//
//
//
//
//  @Override
//  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
//    this.activity = binding.getActivity();
//    binding.addActivityResultListener(this);
//  }
//
//  @Override
//  public void onDetachedFromActivityForConfigChanges() {
//    this.activity = null;
//  }
//
//  @Override
//  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
//    this.activity = binding.getActivity();
//    binding.addActivityResultListener(this);
//  }
//
//  @Override
//  public void onDetachedFromActivity() {
//    this.activity = null;
//  }
//
//  @Override
//  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
////    Log.d("DEBUGINGG", "onActivityResult: working..");
//    if (requestCode == VERIFICATION_RC) {  // ✅ Ensure only our request is handled
//      if (pendingResult == null) {
//        return false;  // ✅ Agar koi pending result nahi hai, to kuch mat karo
//      }
//        if (resultCode == RESULT_OK) {
//          Toast.makeText(activity, "Verified", Toast.LENGTH_SHORT).show();
//          checkIfNumberAlreadyVerified();
//        } else {
//          sendError("user canceled verification message");
//            Toast.makeText(activity, "user_canceled_verification_message", Toast.LENGTH_SHORT).show();
//        }
//        //
////      if (resultCode == Activity.RESULT_OK && data != null) {
////        String verificationId = data.getStringExtra("verification_id");
////        pendingResult.success(verificationId);
////      } else {
////        pendingResult.error("VERIFICATION_FAILED", "User canceled or failed", null);
////      }
//
//      pendingResult = null;  // ✅ Result process hone ke baad reset karo
//      return true;
//    }
//    return false;
//  }
//
//}
//  protected void onResume() {
//    super.onResume();
//    checkIfNumberAlreadyVerified();
//  }
  private void checkIfNumberAlreadyVerified() {
    String verifiedNumber = CheckmobiSdk.getInstance().getVerifiedNumber(activity);
    String serveerId = CheckmobiSdk.getInstance().getVerifiedNumberServerId(activity);
    if (verifiedNumber != null) {
      JSONObject data = new JSONObject();
      try {
        data.put("phoneNumber",verifiedNumber);
        data.put("requestId",serveerId);
        String encodedJson = data.toString();
        verifiedUser(encodedJson);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
//            startActivity(new Intent(this, SuccessActivity.class));
      Log.d("DEBUG", "checkIfNumberAlreadyVerified: success" +verifiedNumber );
      activity.
      finish();
    }else {
      Log.d("FAILED", "checkIfNumberAlreadyVerified: failed");
    }
  }

  private void checkuser(Result result) throws JSONException {
    String verifiedNumber = CheckmobiSdk.getInstance().getVerifiedNumber(activity);
    String serveerId = CheckmobiSdk.getInstance().getVerifiedNumberServerId(activity);
    if (verifiedNumber != null) {
      JSONObject data = new JSONObject();

        data.put("phoneNumber",verifiedNumber);
        data.put("requestId",serveerId);
        String encodedJson = data.toString();


  activity.
              finish();
      result.success(encodedJson);
//      return   verifiedUser(encodedJson);
    }else{
      sendError("Account not found");
      result.error("404","Account not found","no user found");
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
  private void setupCheckmobi() {
    CheckmobiSdk.getInstance().setApiKey(mApiKey);
  }
  private void retrieveLastUsedApiKey() {

    mApiKey =

            activity.getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE).getString(LAST_USED_API_KEY, null);
  }

  private void saveLastUsedApiKey() {
    if (activity == null) {
      Log.e("FlutterCheckmobiPlugin", "Activity is null, cannot start verification.");
      return;
    }
   activity. getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE)
            .edit()
            .putString(LAST_USED_API_KEY, mApiKey)
            .apply();
  }




  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
    binding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    this.activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
    binding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivity() {
    this.activity = null;
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
//    Log.d("DEBUGINGG", "onActivityResult: working..");
    if (requestCode == VERIFICATION_RC) {  // ✅ Ensure only our request is handled
      if (pendingResult == null) {
        return false;  // ✅ Agar koi pending result nahi hai, to kuch mat karo
      }
        if (resultCode == RESULT_OK) {
          Toast.makeText(activity, "Verified", Toast.LENGTH_SHORT).show();
          checkIfNumberAlreadyVerified();
        } else {
          sendError("user canceled verification message");
            Toast.makeText(activity, "user_canceled_verification_message", Toast.LENGTH_SHORT).show();
        }
        //
//      if (resultCode == Activity.RESULT_OK && data != null) {
//        String verificationId = data.getStringExtra("verification_id");
//        pendingResult.success(verificationId);
//      } else {
//        pendingResult.error("VERIFICATION_FAILED", "User canceled or failed", null);
//      }

      pendingResult = null;  // ✅ Result process hone ke baad reset karo
      return true;
    }
    return false;
  }

}
