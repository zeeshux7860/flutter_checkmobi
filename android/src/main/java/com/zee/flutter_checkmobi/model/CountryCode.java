package com.zee.flutter_checkmobi.model;

import com.google.gson.annotations.SerializedName;

public class CountryCode {

    @SerializedName("prefix")
    private String prefix;
    @SerializedName("name")
    private String name;
    
    @SerializedName("flag_128")
    private String url;
    
    public CountryCode(String prefix, String name, String url) {
        this.prefix = prefix;
        this.name = name;
        this.url = url;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
