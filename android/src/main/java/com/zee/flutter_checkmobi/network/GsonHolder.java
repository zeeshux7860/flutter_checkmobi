package com.zee.flutter_checkmobi.network;

import com.google.gson.Gson;

public class GsonHolder {
    
    private final static Gson gson = new Gson();
    
    private GsonHolder() {}
    
    public static Gson getGson() {
        return gson;
    }
}
