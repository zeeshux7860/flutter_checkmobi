package com.zee.flutter_checkmobi.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.flutter.embedding.android.FlutterFragment;

public class CheckmobiBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cached engine ko use kar rahe hain
        FlutterFragment flutterFragment = FlutterFragment.withCachedEngine("checkmobi_engine")
                .build();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, flutterFragment)
                .commit();
    }
}
