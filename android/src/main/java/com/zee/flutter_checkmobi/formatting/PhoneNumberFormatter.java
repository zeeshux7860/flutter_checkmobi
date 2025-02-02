package com.zee.flutter_checkmobi.formatting;


import android.util.Log;

import com.google.gson.Gson;
import com.zee.flutter_checkmobi.CheckmobiSdk;
import com.zee.flutter_checkmobi.model.CountryCode;
import com.zee.flutter_checkmobi.network.RetrofitController;
import com.zee.flutter_checkmobi.network.request.CheckNumberRequestBody;
import com.zee.flutter_checkmobi.network.response.CheckNumberResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Request;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;

public class PhoneNumberFormatter {
    
    @NonNull
    public static String getFormattedCountryCode(@NonNull CountryCode countryCode) {
        return countryCode.getName() + " (+" + countryCode.getPrefix() + ")";
    }
    
    @Nullable
    public static String getLocallyFormattedPhoneNumber(@Nullable CountryCode countryCode, @Nullable String number) {
        if (countryCode != null && number != null) {
            return "+" + countryCode.getPrefix() + number;
        } else {
            return null;
        }
    }

    public static void checkAndFormatNumber(@NonNull CountryCode countryCode, @NonNull String number, @NonNull Callback<CheckNumberResponse> callback) {
        String formattedPhoneNumber = getLocallyFormattedPhoneNumber(countryCode, number);
//        CheckNumberRequestBody requestBody = new CheckNumberRequestBody(formattedPhoneNumber, Locale.getDefault().getLanguage());
//        Log.d("REQUEST_BODY_OBJECT", "Request Body Object: " + requestBody.toString());
//        Gson gson = new Gson();
//        String jsonBody = gson.toJson(requestBody);
//        Log.d("REQUEST_BODY_JSON", "Request Body JSON: " + jsonBody);

        if (formattedPhoneNumber != null) {
            Call<CheckNumberResponse> repos = RetrofitController.getInstance().getService()
                    .checkNumber(CheckmobiSdk.getInstance().getApiKey(),
                            new CheckNumberRequestBody(formattedPhoneNumber, Locale.getDefault().getLanguage()));
            // ✅ Log Request Details
//            Request request = repos.request();
//            Log.d("REQUEST_URL", "URL: " + request.url());
//            Log.d("REQUEST_METHOD", "Method: " + request.method());
//            Log.d("REQUEST_HEADERS", "Headers: " + request.headers());
//
//            // ✅ Log Request Body (JSON Format)
//            try {
//                if (request.body() != null) {
//                    Buffer buffer = new Buffer();
//                    request.body().writeTo(buffer);
////                    String requestBody = buffer.readUtf8();
//                    Log.d("REQUEST_BODY", "Body: " + requestBody);
//                } else {
//                    Log.d("REQUEST_BODY", "No Body Sent");
//                }
//            } catch (IOException e) {
//                Log.e("REQUEST_BODY", "Error reading request body", e);
//            }
            repos.enqueue(callback);
        } else {
            callback.onFailure(null, new Throwable("Country Code or Number null!"));
        }
    }
}
