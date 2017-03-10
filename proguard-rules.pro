# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/caro/Library/Android/sdk/tools/proguard/proguard-android.txt
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

-keep class com.jpardogo.android.googleprogressbar.** { *; }
-keep class com.jpardogo.android.googleprogressbar.** { *; }

#http://square.github.io/retrofit/
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#配置proguard.cfg以允许你的GlideModule可以通过反射实例化
-keepnames class * com.caro.smartmodule.ImageLoader.SmartGlideModule
#或者把所有的Module都一次性配置好
-keep public class * implements com.bumptech.glide.module.GlideModule