package com.zee.flutter_checkmobi.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zee.flutter_checkmobi.CheckmobiSdk;
import com.zee.flutter_checkmobi.formatting.PhoneNumberFormatter;
import com.zee.flutter_checkmobi.model.CountryCode;
import com.zee.flutter_checkmobi.network.response.CheckNumberResponse;
import com.zee.flutter_checkmobi.network.response.ValidationResponse;
import com.zee.flutter_checkmobi.storage.StorageController;
import com.zee.flutter_checkmobi.system.listeners.CallListener;

import com.zee.flutter_checkmobi.validation.ValidationController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberInputActivity extends VerificationBaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PIN_VALIDATION_REQUEST_CODE = 2;
    private CheckNumberResponse lastFullNumberUsed;
    private CountDownTimer countDownTimer;

    private CallListener mCallListener;
    private Context myContext;
    private String savedCountryCode;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the data from Intent
         savedCountryCode = getIntent().getStringExtra("savedCountryCode");
         number = getIntent().getStringExtra("number");

        Log.d("COUNTRYCODE", "onCreate: "+savedCountryCode);
        Log.d("number", "onCreate: "+number);

        registerReceivers();

        checkAndFormatNumber();

    }

    @Override
    protected boolean listenForSms() {
        return false;
    }

    @Override
    protected boolean listenForCall() {
        return true;
    }
    private void registerReceivers() {
        mCallListener = new CallListener();
        registerReceiver(mCallListener, new IntentFilter("android.intent.action.PHONE_STATE"));
    }

    private void requestFirstValidation(CheckNumberResponse fullNumberUsed) {
        Log.d("NOTWORKING", "requestFirstValidation: runing");

        final CheckNumberResponse.ValidationMethod validationMethod = ValidationController.getFirstAvailableValidationMethod(myContext, fullNumberUsed);
        Log.d("NOTWORKING", "requestFirstValidation: runing");
System.out.print("------------------------aaa");
        if (validationMethod != null) {
            if (validationMethod.getType().equals(CheckNumberResponse.ValidationMethod.REVERSE_CLI) && (getGrantForPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || getGrantForPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)) {
                handleReverseCliFailed();
            } else {

                requestValidation(validationMethod.getType(), false);
            }
        } else {
            Toast.makeText(myContext, "No more validation methods", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleReverseCliFailed() {
        CheckNumberResponse lastUsedFullNumber = StorageController.getInstance().getLastUsedFullNumber();
        if (ValidationController.isValidationMethodValid(myContext, CheckNumberResponse.ValidationMethod.SMS, lastUsedFullNumber)) {
            requestValidation(CheckNumberResponse.ValidationMethod.SMS, true);
        }
    }

    private int getGrantForPermission(String permission) {
        return ContextCompat.checkSelfPermission(myContext, permission);

    }


    public void checkAndFormatNumber() {
        myContext = this;
        if (savedCountryCode == null) {
            showErrorDialog(com.zee.flutter_checkmobi.R.string.country_code_empty_error);
        } else if (TextUtils.isEmpty(number)) {
            showErrorDialog(com.zee.flutter_checkmobi.R.string.number_empty_error);
        } else {
            CountryCode countryCode = new CountryCode("+" + savedCountryCode,"", ""); // Output: CountryCode object for +91
            showLoading();
            StorageController.getInstance().updateLastUsedNumber(this, number);
            PhoneNumberFormatter.checkAndFormatNumber(countryCode, number, new Callback<CheckNumberResponse>() {
                @Override
                public void onResponse(Call<CheckNumberResponse> call, Response<CheckNumberResponse> response) {
                    hideLoading();
                    if(response.isSuccessful()) {
                        // VALIDATION API
                        showValidNumberVerificationDialog(response.body());
                    } else {

                        String locallyFormattedPhoneNumber = savedCountryCode + number;
                        Log.d("DEBUG", CheckmobiSdk.getInstance().getApiKey());
                        showInvalidNumberVerificationDialog(CheckNumberResponse.createResponseFromE164Number(locallyFormattedPhoneNumber));
                    }
                }

                @Override
                public void onFailure(Call<CheckNumberResponse> call, Throwable t) {
                    hideLoading();
                    String locallyFormattedPhoneNumber = savedCountryCode + number;
                    Log.d("DEBUG", locallyFormattedPhoneNumber.toString());
                    showInvalidNumberVerificationDialog(CheckNumberResponse.createResponseFromE164Number(locallyFormattedPhoneNumber));
                }
            });
        }
    }


    private void showValidNumberVerificationDialog(final CheckNumberResponse checkNumberResponse) {
        Log.d("RUNING", checkNumberResponse.toString());
        Log.d("DEBUG", checkNumberResponse.toString());
        lastFullNumberUsed = checkNumberResponse;
        StorageController.getInstance().updateLastUsedFullNumber(checkNumberResponse);
        showMissedCallTutorialIfNecessary(checkNumberResponse);
//        showNumberVerificationDialog(com.zee.flutter_checkmobi.R.string.valid_number_verification_message, checkNumberResponse);
    }
    private void showMissedCallTutorialIfNecessary(final CheckNumberResponse checkNumberResponse) {
        requestCallPermission(checkNumberResponse);
    }


    private void requestCallPermission(CheckNumberResponse fullNumberUsed) {
        String callPermission = Manifest.permission.READ_PHONE_STATE;
        String callLogPermission = Manifest.permission.READ_CALL_LOG;
        int callGrant = getGrantForPermission(callPermission);
        int callLogGrant = getGrantForPermission(callLogPermission);
        List<String> permissionList = new ArrayList<>();
        if (callFirst(fullNumberUsed)) {
            if (callGrant != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(callPermission);
            }
            if (callLogGrant != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(callLogPermission);
            }
        }
        Log.d("CHECKPERMISSION", "LOADED");
        if (permissionList.size() > 0) {

            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), PERMISSION_REQUEST_CODE);
        } else {
            Log.d("HIT HERE", String.valueOf(fullNumberUsed));
            requestFirstValidation(fullNumberUsed);
        }
    }
    private void showInvalidNumberVerificationDialog(final CheckNumberResponse checkNumberResponse) {
        System.out.print(checkNumberResponse);


//        showNumberVerificationDialog(com.zee.flutter_checkmobi.R.string.invalid_number_verification_message, checkNumberResponse);
    }

    private void requestValidation(final String validationType, final boolean silentFail) {
        ValidationController.validate(NumberInputActivity.this, validationType, lastFullNumberUsed.getE164Format(), new Callback<ValidationResponse>() {
            @Override
            public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {


                if (response.isSuccessful()) {
                    if (validationType.equals(CheckNumberResponse.ValidationMethod.REVERSE_CLI)) {
                        setCallCountDownTimer();
                    }
                } else {
                    if (silentFail) {
                        //DO NOTHING
                    } else {
                        showErrorDialog(com.zee.flutter_checkmobi.R.string.server_error);
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidationResponse> call, Throwable t) {
                System.out.println(t.toString());
                showErrorDialog(com.zee.flutter_checkmobi.R.string.server_error);
            }
        });
    }
    private void setCallCountDownTimer() {


        showLoading();
        countDownTimer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Do Nothing
            }

            public void onFinish() {
                hideLoading();
                handleReverseCliFailed();
            }
        };
        countDownTimer.start();
        Log.d("RUNING", "TESTINGF..........................");

    }

    private boolean callFirst(CheckNumberResponse fullNumberUsed) {
        CheckNumberResponse.ValidationMethod firstAvailableValidationMethod = ValidationController.getFirstAvailableValidationMethod(this, fullNumberUsed);
        if (firstAvailableValidationMethod != null) {
            return firstAvailableValidationMethod.getType().equals(CheckNumberResponse.ValidationMethod.REVERSE_CLI);
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("DESTRY", "Clean");
        unregisterReceiver(mCallListener);
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Ensure timer is stopped when activity is destroyed
        }
        super.onDestroy();
    }

    @Override
    protected void handleSuccessfulVerification() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.handleSuccessfulVerification();
    }
}
