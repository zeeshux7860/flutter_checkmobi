package com.zee.flutter_checkmobi.ui;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.zee.flutter_checkmobi.model.LastValidation;
import com.zee.flutter_checkmobi.network.response.CheckNumberResponse;
import com.zee.flutter_checkmobi.storage.StorageController;
import com.zee.flutter_checkmobi.system.listeners.CallListener;
import com.zee.flutter_checkmobi.R;
import com.zee.flutter_checkmobi.system.listeners.SmsListener;
import com.zee.flutter_checkmobi.network.response.VerificationResponse;
import com.zee.flutter_checkmobi.validation.ValidationController;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class VerificationBaseActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLoading();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerBroadcastReceiverNew();
        }else{
            registerBroadcastReceiver();
        }
    }
    private ProgressDialog progress;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Receiver", "Intent received: " + intent.getAction());
            if (intent.getAction().equals(SmsListener.SMS_TO_VERIFY) || intent.getAction().equals(CallListener.CALL_TO_VERIFY)) {
                final String pin = intent.getStringExtra(ValidationController.PIN_EXTRA);
                String id = intent.getStringExtra(ValidationController.ID_EXTRA);
                Log.d("Receiver", "Pin: " + pin + " , ID: " + id);
                verifyPin(pin, id);
            }
        }
    };

    protected void verifyPin(final String pin, String id) {
        System.out.println("verifyPin");
        if (!TextUtils.isEmpty(pin) && !TextUtils.isEmpty(id)) {
            System.out.println("Verifying pin: " + pin);
            showLoading();
            ValidationController.verify(id, pin, new Callback<VerificationResponse>() {
                @Override
                public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                    hideLoading();
                    System.out.println(response);
                    if (response.isSuccessful() && response.body().isValidated()) {
                        handleSuccessfulVerification();
                        System.out.println("Verification Complete for pin: " + pin);
                    } else {
                        showErrorDialog(R.string.incorrect_pin);
                    }
                }

                @Override
                public void onFailure(Call<VerificationResponse> call, Throwable t) {
                    hideLoading();
                    showErrorDialog(R.string.incorrect_pin);
                    System.out.println(t.getMessage());
                }
            });
        }
    }



//    @Override
//    public void onBackPressed() {
//        showCancelVerificationDialog();
//    }

    @Override
    protected void onDestroy() {
        android.util.Log.d("UNREGESTERTED", "onDestroy: done");
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void registerBroadcastReceiver() {
        if (listenForSms()) {
            registerReceiver(receiver, new IntentFilter(SmsListener.SMS_TO_VERIFY));
        }
        if (listenForCall()) {
            registerReceiver(receiver, new IntentFilter(CallListener.CALL_TO_VERIFY));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void registerBroadcastReceiverNew() {

        if (listenForSms()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.
                registerReceiver(receiver, new IntentFilter(SmsListener.SMS_TO_VERIFY), Context.RECEIVER_EXPORTED);
            }
        }
        if (listenForCall()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                android.util.Log.d("REGISTER", "registerBroadcastReceiverNew: done");
                this.  registerReceiver(receiver, new IntentFilter(CallListener.CALL_TO_VERIFY), Context.RECEIVER_EXPORTED);
            }
        }
    }

    protected abstract boolean listenForSms();

    protected abstract boolean listenForCall();

    private void unregisterBroadcastReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void setupLoading() {
        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.verifying_phone_number));
        progress.setMessage(getResources().getString(R.string.please_wait));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
    }

    protected void showLoading() {
        if (progress != null && !progress.isShowing()) {
            progress.show();
        }
    }

    protected void hideLoading() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    protected void showErrorDialog(int id) {
        showErrorDialog(getResources().getString(id));
    }

    protected void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void showCancelVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getResources().getString(R.string.cancel_verification_message));
        builder.setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handleCanceledVerification();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.button_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    protected void handleSuccessfulVerification() {
        CheckNumberResponse lastUsedFullNumber = StorageController.getInstance().getLastUsedFullNumber();
        if (lastUsedFullNumber != null) {
            String verifiedNumber = lastUsedFullNumber.getE164Format();
            StorageController.getInstance().saveVerifiedNumber(this, verifiedNumber);
        }
        LastValidation lastValidation = StorageController.getInstance().getLatestLastValidation();
        if (lastValidation != null && lastValidation.getValidationResponse() != null) {
            String verifiedNumberServerId = lastValidation.getValidationResponse().getId();
            StorageController.getInstance().saveVerifiedNumberServerId(this, verifiedNumberServerId);
        }
        StorageController.getInstance().resetInMemoryStorage();
        setResult(RESULT_OK);
        finish();
    }

    private void handleCanceledVerification() {
        StorageController.getInstance().resetInMemoryStorage();
        setResult(RESULT_CANCELED);
        finish();
    }

    protected void registerForSMSRetrieverApi() {
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Successfully started retriever, expect broadcast intent");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed to start retriever, inspect Exception for more details");
            }
        });
    }
}