# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yingnanli/Library/Android/sdk/tools/proguard/proguard-android.txt
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

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-allowaccessmodification

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose


-optimizationpasses 3
-optimizations !class/merging/horizontal,!code/allocation/variable

#-applymapping  ./build/outputs/mapping/mapping.txt

-dontnote
#-printusage ./build/outputs/mapping/release/shrink.txt
#-libraryjars ./proguard_libraryjars/


-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keepclassmembers class **.R$* {
#    public static <fields>;
#}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

########################################proguard setting from android sdk.[end]#########################################

########################################proguard setting for this project.[start]#######################################
#-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
#-ignorewarning


# Gson
-keep class com.google.** { *; }
-keep interface com.google.** { *; }
-keep enum com.google.** { *; }

# facebook
-keep class com.facebook.** { *; }
-keep interface com.facebook.** { *; }
-keep enum com.facebook.** { *; }

########### linkv rtc ###########
-keep class com.linkv.rtc.entity.** { *; }
-keep class com.linkv.rtc.internal.jnibridge.** { *; }
-keep class com.linkv.rtc.internal.entity.LVAndroidDeviceInfo { *; }
-keep class com.linkv.rtc.internal.network.LVHttpUtils { *; }
-keep class com.linkv.rtc.internal.src.** { *; }
-keep class com.app.rtcsdk.** { *; }
-dontwarn com.cmcm.cmrtc.**
-dontwarn com.app.rtcsdk.**
-ignorewarnings

#zego
-keep class com.zego.** { *; }

#eventbus
-keep public class * extends * {
    public void onEvent*(...);
}

# keep some data set classes.
-keep class * extends de.greenrobot.dao.AbstractDao {*;}
-keep class * implements com.kxsimon.db.Keepbase {*;}


-keepattributes Exceptions,InnerClasses


-keepattributes Signature

-keepattributes *Annotation*
