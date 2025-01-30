package com.zee.flutter_checkmobi.network.response;

import com.google.gson.annotations.SerializedName;

public class VerificationResponse {

    @SerializedName("validated")
    private boolean validated;
    
    public boolean isValidated() {
        return validated;
    }
}
