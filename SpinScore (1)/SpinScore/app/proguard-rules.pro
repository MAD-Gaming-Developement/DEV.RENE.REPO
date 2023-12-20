-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }

-keep class com.airbnb.lottie.** { *; }
-keep class com.android.volley.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

-keepclassmembers class com.mgd.mgddevtools.** {
    *;
}

-keepclassmembers class .R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.google.ads.** { *; }

-dontwarn java.awt.Color
-dontwarn java.awt.Font
-dontwarn java.awt.Point
-dontwarn com.google.firebase.iid.FirebaseInstanceId
-dontwarn com.google.firebase.iid.InstanceIdResult
-dontwarn com.google.firebase.messaging.FirebaseMessagingService
-dontwarn com.google.firebase.messaging.RemoteMessage$Notification
-dontwarn com.google.firebase.messaging.RemoteMessage