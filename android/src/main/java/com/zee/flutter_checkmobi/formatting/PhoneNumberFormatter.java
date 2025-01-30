package com.zee.flutter_checkmobi.formatting;


import com.zee.flutter_checkmobi.CheckmobiSdk;
import com.zee.flutter_checkmobi.model.CountryCode;
import com.zee.flutter_checkmobi.network.RetrofitController;
import com.zee.flutter_checkmobi.network.request.CheckNumberRequestBody;
import com.zee.flutter_checkmobi.network.response.CheckNumberResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

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
        if (formattedPhoneNumber != null) {
            Call<CheckNumberResponse> repos = RetrofitController.getInstance().getService()
                    .checkNumber(CheckmobiSdk.getInstance().getApiKey(),
                            new CheckNumberRequestBody(formattedPhoneNumber, Locale.getDefault().getLanguage()));
            repos.enqueue(callback);
        } else {
            callback.onFailure(null, new Throwable("Country Code or Number null!"));
        }
    }
}
