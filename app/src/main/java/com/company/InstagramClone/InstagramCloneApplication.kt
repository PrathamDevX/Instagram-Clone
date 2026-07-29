package com.company.InstagramClone

import android.app.Application
import com.cloudinary.android.MediaManager
import android.util.Log

class InstagramCloneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initCloudinary()
    }

    private fun initCloudinary() {
        val config = mapOf(
            "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
            "api_key" to BuildConfig.CLOUDINARY_API_KEY,
            "api_secret" to BuildConfig.CLOUDINARY_API_SECRET,
            "secure" to true
        )
        
        try {
            MediaManager.init(this, config)
            Log.d("Cloudinary", "Cloudinary initialized successfully with cloud name: ${BuildConfig.CLOUDINARY_CLOUD_NAME}")
        } catch (e: Exception) {
            Log.e("Cloudinary", "Failed to initialize Cloudinary: ${e.message}")
        }
    }
}
