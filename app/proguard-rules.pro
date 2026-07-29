# Cloudinary
-keep class com.cloudinary.** { *; }
-dontwarn com.cloudinary.**

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# CameraX
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# Glide
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public class * extends com.bumptech.glide.module.LibraryGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
