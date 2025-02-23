package com.zee.flutter_checkmobi.storage;

import com.zee.flutter_checkmobi.model.CountryCode;
import com.zee.flutter_checkmobi.model.LastValidation;
import com.zee.flutter_checkmobi.network.response.CheckNumberResponse;
import com.zee.flutter_checkmobi.network.response.ValidationResponse;

import android.content.Context;

import java.util.List;

public class StorageController {
    
    private static final StorageController instance = new StorageController();
    private SharedPreferencesStorage mSharedPreferencesStorage;
    private InMemoryStorage mInMemoryStorage;
    
    private StorageController() {
        mSharedPreferencesStorage = new SharedPreferencesStorage();
        mInMemoryStorage = new InMemoryStorage();
    }
    
    public static StorageController getInstance() {
        return instance;
    }
    
    public void saveCountryCode(Context context, CountryCode countryCode) {
        mSharedPreferencesStorage.saveCountryCode(context, countryCode);
    }
    
    public CountryCode readCountryCode(Context context) {
        return mSharedPreferencesStorage.readCountryCode(context);
    }
    
    public void updateLastValidation(String validationType, ValidationResponse validationResponse) {
        mInMemoryStorage.updateLastValidation(validationType, validationResponse);
    }
    
    public LastValidation readLastValidationForType(String validationType) {
        return mInMemoryStorage.readLastValidationForType(validationType);
    }
    
    public LastValidation getLatestLastValidation() {
        return mInMemoryStorage.getLatestLastValidation();
    }
    
    public void updateLastUsedNumber(Context context, String number) {
        mSharedPreferencesStorage.updateLastUsedNumber(context, number);
    }
    
    public String getLastUsedNumber(Context context) {
        return mSharedPreferencesStorage.getLastUsedNumber(context);
    }
    
    public void updateLastUsedFullNumber(CheckNumberResponse lastFullNumber) {
        mInMemoryStorage.updateLastUsedFullNumber(lastFullNumber);
    }
    
    public CheckNumberResponse getLastUsedFullNumber() {
        return mInMemoryStorage.getLastUsedFullNumber();
    }
    
    public List<CheckNumberResponse.ValidationMethod> getLastUsedValidationMethods() {
        CheckNumberResponse lastUsedFullNumber = getLastUsedFullNumber();
        if (lastUsedFullNumber != null) {
            return lastUsedFullNumber.getValidationMethods();
        } else {
            return CheckNumberResponse.getDefaultVerificationMethods();
        }
    }
    
    public String getSmsTemplateFromLastUsedValidationMethods() {
        List<CheckNumberResponse.ValidationMethod> lastUsedValidationMethods = getLastUsedValidationMethods();
        for (CheckNumberResponse.ValidationMethod validationMethod : lastUsedValidationMethods) {
            if (validationMethod.getType().equals(CheckNumberResponse.ValidationMethod.SMS)) {
                return validationMethod.getSmsTemplate();
            }
        }
        return CheckNumberResponse.DEFAULT_SMS_TEMPLATE;
    }
    
    public String getAndroidAppHashFromLastUsedValidationMethods() {
        List<CheckNumberResponse.ValidationMethod> lastUsedValidationMethods = getLastUsedValidationMethods();
        for (CheckNumberResponse.ValidationMethod validationMethod : lastUsedValidationMethods) {
            if (validationMethod.getType().equals(CheckNumberResponse.ValidationMethod.SMS)) {
                return validationMethod.getAndroidAppHash();
            }
        }
        return null;
    }
    
    public void resetInMemoryStorage() {
        mInMemoryStorage.reset();
    }
    
    public void saveVerifiedNumberServerId(Context context, String verifiedNumberServerId) {
        mSharedPreferencesStorage.saveVerifiedNumberServerId(context, verifiedNumberServerId);
    }
    
    public String getVerifiedNumberServerId(Context context) {
        return mSharedPreferencesStorage.getVerifiedNumberServerId(context);
    }
    
    public void saveVerifiedNumber(Context context, String verifiedNumber) {
        mSharedPreferencesStorage.saveVerifiedNumber(context, verifiedNumber);
    }
    
    public String getVerifiedNumber(Context context) {
        return mSharedPreferencesStorage.getVerifiedNumber(context);
    }
    
    public void resetVerifiedNumber(Context context) {
        mSharedPreferencesStorage.resetVerifiedNumber(context);
    }
}
