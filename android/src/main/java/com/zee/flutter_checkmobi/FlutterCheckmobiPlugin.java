package com.zee.flutter_checkmobi;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterCheckmobiPlugin */
public class FlutterCheckmobiPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Activity activity;
  private static MethodChannel channel;
  private static String mApiKey = "";
  private static final String CHECKMOBI_SECRET_KEY = "";

  private static final String SHARED_PREFS_FILE = "android_checkmobi_sample_prefs";
  private static final String LAST_USED_API_KEY = "last_used_api_key";

  private static final int VERIFICATION_RC = 1;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_checkmobi");
    channel.setMethodCallHandler(this);
    // Wait until activity is available


      setupCheckmobi();
      saveLastUsedApiKey();

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    Log.d("CALLINGTESTING", "onMethodCall: "+call.method);
    if (call.method.equals("callIInitialize")) {
      String id = call.argument("id");
      String phoneNumner = call.argument("phoneNumner");
      String countryCode = call.argument("countryCode");
      startVerificationProcess(phoneNumner,countryCode,id);
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else
    if (call.method.equals("sendData")) {
      sendDataToFlutter("okokoko");
      result.success("Android " + android.os.Build.VERSION.RELEASE);
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

    System.out.println("Verification process started in MainActivity!");
  }
//  @Override
//  protected void onResume() {
//    super.onResume();
//    checkIfNumberAlreadyVerified();
//  }
  private void checkIfNumberAlreadyVerified() {
    String verifiedNumber = CheckmobiSdk.getInstance().getVerifiedNumber(activity);
    String serveerId = CheckmobiSdk.getInstance().getVerifiedNumberServerId(activity);
    if (verifiedNumber != null) {
//            startActivity(new Intent(this, SuccessActivity.class));
      Log.d("DEBUG", "checkIfNumberAlreadyVerified: success" +verifiedNumber );
      activity.
      finish();
    }else {
      Log.d("FAILED", "checkIfNumberAlreadyVerified: failed");
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


//  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == VERIFICATION_RC) {
      if (resultCode == RESULT_OK) {
        Toast.makeText(activity, "Verified", Toast.LENGTH_SHORT).show();
activity.
        finish();
      } else {
        Toast.makeText(activity, "user_canceled_verification_message", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    this.activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    this.activity = null;
  }
}
