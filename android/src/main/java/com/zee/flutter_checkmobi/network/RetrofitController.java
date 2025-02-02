package com.zee.flutter_checkmobi.network;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitController {
    
    private Retrofit retrofit;
    private CheckMobiServiceInterface service;
    
    private static final RetrofitController instance = new RetrofitController();
    
    private RetrofitController() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.checkmobi.com")
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson())) // Gson setup
                .build();
    
        service = retrofit.create(CheckMobiServiceInterface.class);
    };
    
    public static RetrofitController getInstance() {
        return instance;
    }
    
    public CheckMobiServiceInterface getService() {
        return service;
    }
    
}
