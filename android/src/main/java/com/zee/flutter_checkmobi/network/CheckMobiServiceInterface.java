package com.zee.flutter_checkmobi.network;

import com.zee.flutter_checkmobi.model.CountryCode;
import com.zee.flutter_checkmobi.network.request.CheckNumberRequestBody;
import com.zee.flutter_checkmobi.network.request.ValidationRequestBody;
import com.zee.flutter_checkmobi.network.request.VerificationRequestBody;
import com.zee.flutter_checkmobi.network.response.CheckNumberResponse;
import com.zee.flutter_checkmobi.network.response.ValidationResponse;
import com.zee.flutter_checkmobi.network.response.VerificationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CheckMobiServiceInterface {
    
    @GET("v1/countries")
    Call<List<CountryCode>> getCountries();
    
    @POST("v1/validation/remote-config")
    Call<CheckNumberResponse> checkNumber(@Header("Authorization") String authorization, @Body CheckNumberRequestBody checkNumberRequestBody);
    
    @POST("v1/validation/request")
    Call<ValidationResponse> validate(@Header("Authorization") String authorization, @Body ValidationRequestBody validationRequestBody);
    
    @POST("v1/validation/verify")
    Call<VerificationResponse> verify(@Header("Authorization") String authorization, @Body VerificationRequestBody verificationRequestBody);
    
}
