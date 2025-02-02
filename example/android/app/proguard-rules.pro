# Retrofit related rules
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# Gson related rules
-keep class com.google.gson.** { *; }

# Keep model classes (if any) to avoid being obfuscated or removed
-keep class com.zee.flutter_checkmobi.model.** { *; }
