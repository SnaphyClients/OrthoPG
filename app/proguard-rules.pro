# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Ravi-Gupta\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-dontwarn org.joda.convert.**

-keep class  com.strongloop.** { *; }
-keep class com.androidsdk.snaphy.** { *; }


-keep public class * implements butterknife.Unbinder { public <init>(...); }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-keep class com.synnapps.carouselview.** { *; }

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe


-keep public class com.clevertap.android.* { public *;}
-dontwarn com.clevertap.android.**

-keepattributes EnclosingMethod
