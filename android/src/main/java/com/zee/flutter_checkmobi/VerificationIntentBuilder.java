package com.zee.flutter_checkmobi;

import android.content.Context;
import android.content.Intent;

import com.zee.flutter_checkmobi.ui.NumberInputActivity;

public class VerificationIntentBuilder {

    private int mTheme = -1;

    public VerificationIntentBuilder setTheme(int theme) {
        mTheme = theme;
        return this;
    }

    public Intent build(Context context,
                        String phoneNumber,
                        String countryCode,
                        String id
                        ) {
        Intent intent = new Intent(context, NumberInputActivity.class);
        intent.putExtra("number", phoneNumber);
        intent.putExtra("savedCountryCode", countryCode);
        intent.putExtra("id", id);
//        if (mTheme != -1) {
//            intent.putExtra(CheckmobiBaseActivity.EXTRA_CUSTOM_THEME, mTheme);
//        }
        return intent;
    }

}