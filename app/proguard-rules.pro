# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Application class
-keep class com.example.weatherclient.WeatherApp { *; }

# Keep all classes with @HiltAndroidApp
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

# Keep all classes with @AndroidEntryPoint
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# Hilt rules
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelFactoriesEntryPoint

# Keep Hilt generated classes
-keep class **_HiltComponents$* { *; }
-keep class **_Hilt* { *; }
-keep class **.*_Factory { *; }
-keep class **.*_MembersInjector { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes EnclosingMethod

# Moshi
-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonClass class *
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }