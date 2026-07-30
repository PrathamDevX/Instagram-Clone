package com.company.InstagramClone.utils

/**
 * Utility to optimize Cloudinary delivery URLs
 */
object CloudinaryHelper {

    /**
     * Injects optimization parameters (f_auto, q_auto) and optional resizing into a Cloudinary URL.
     * Example: .../upload/v123/... -> .../upload/f_auto,q_auto,w_500/v123/...
     */
    fun getOptimizedUrl(
        url: String,
        width: Int? = null,
        height: Int? = null,
        crop: String = "fill"
    ): String {
        if (!url.contains("cloudinary.com") || url.contains("f_auto")) return url

        val uploadPart = "/upload/"
        if (!url.contains(uploadPart)) return url

        val sb = StringBuilder()
        sb.append("f_auto,q_auto:good")
        
        if (width != null) sb.append(",w_$width")
        if (height != null) sb.append(",h_$height")
        if (width != null || height != null) sb.append(",c_$crop")

        return url.replace(uploadPart, "$uploadPart${sb}/")
    }

    /**
     * Specifically for profile grid thumbnails (small, square)
     */
    fun getThumbnailUrl(url: String): String = getOptimizedUrl(url, width = 300, height = 300)

    /**
     * Specifically for home feed posts (high quality, responsive width)
     */
    fun getFeedUrl(url: String): String = getOptimizedUrl(url, width = 1080)

    /**
     * Optimizes video delivery: auto format, auto quality, cap at 720p
     */
    fun getOptimizedVideoUrl(url: String): String {
        if (!url.contains("cloudinary.com") || url.contains("f_auto")) return url
        
        val uploadPart = "/upload/"
        if (!url.contains(uploadPart)) return url

        // For videos, we want f_auto, q_auto and cap resolution at 720p for smoothness
        return url.replace(uploadPart, "${uploadPart}f_auto,q_auto,w_720,c_limit/")
    }

    /**
     * Detects media type from Cloudinary URL structure or extension
     */
    fun getMediaType(url: String, currentType: String): String {
        if (currentType == "video") return "video"
        
        val videoPatterns = listOf("/video/upload/", ".mp4", ".mov", ".avi", ".webm")
        if (videoPatterns.any { url.contains(it, ignoreCase = true) }) {
            return "video"
        }
        
        return "image"
    }
}
