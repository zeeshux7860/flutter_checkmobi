package com.zee.flutter_checkmobi_example;

import android.app.Application;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.view.FlutterMain;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Flutter engine ko initialize kar rahe hain
        FlutterEngine flutterEngine = new FlutterEngine(this);

        // Initial route set kar rahe hain
        flutterEngine.getNavigationChannel().setInitialRoute("/verification_screen");

        // Dart entrypoint ko execute kar rahe hain
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );

        // Engine ko register kar rahe hain, taki "checkmobi_engine" naam se use ho sake
        FlutterEngineCache.getInstance().put("checkmobi_engine", flutterEngine);
    }
}
