package com.company.InstagramClone.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.preprocess.BitmapEncoder
import com.cloudinary.android.preprocess.ImagePreprocessChain
import com.company.InstagramClone.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MediaRepository {

    suspend fun uploadImage(context: Context, uri: Uri): Result<String> = uploadMedia(context, uri, "image")

    suspend fun uploadVideo(context: Context, uri: Uri): Result<String> = uploadMedia(context, uri, "video")

    private suspend fun uploadMedia(context: Context, uri: Uri, type: String): Result<String> = suspendCancellableCoroutine { continuation ->
        Log.d("Cloudinary", "Starting $type upload for Uri: $uri")
        
        var request = MediaManager.get().upload(uri)
            .unsigned(BuildConfig.CLOUDINARY_UPLOAD_PRESET)
            .option("resource_type", type)
            .option("chunk_size", 6000000) // Must be at least 5MB (5,242,880 bytes)

        if (type == "image") {
            request = request.preprocess(
                ImagePreprocessChain.limitDimensionsChain(720, 720) // Faster upload with 720p
                    .saveWith(BitmapEncoder(BitmapEncoder.Format.JPEG, 70)) // Increased compression
            )
        }

        request.callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                Log.d("Cloudinary", "Upload started: $requestId")
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                val progress = (bytes.toDouble() / totalBytes) * 100
                Log.d("Cloudinary", "Upload progress: ${progress.toInt()}%")
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                val url = resultData["secure_url"] as? String
                if (url != null) {
                    Log.d("Cloudinary", "Upload success: $url")
                    if (continuation.isActive) continuation.resume(Result.success(url))
                } else {
                    Log.e("Cloudinary", "Upload success but URL is null")
                    if (continuation.isActive) continuation.resume(Result.failure(Exception("URL not found in result")))
                }
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                Log.e("Cloudinary", "Upload failed: ${error.description}")
                if (continuation.isActive) continuation.resume(Result.failure(Exception(error.description)))
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                Log.d("Cloudinary", "Upload rescheduled: ${error.description}")
            }
        }).dispatch(context)
    }
}
