package com.company.InstagramClone.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@OptIn(UnstableApi::class)
object VideoCache {
    private var simpleCache: SimpleCache? = null
    private val lock = Any()

    fun getInstance(context: Context): SimpleCache? {
        synchronized(lock) {
            if (simpleCache == null) {
                try {
                    val cacheDir = File(context.cacheDir, "video_cache")
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs()
                    }
                    val evictor = LeastRecentlyUsedCacheEvictor(200 * 1024 * 1024) // 200MB cache
                    val databaseProvider = StandaloneDatabaseProvider(context)
                    simpleCache = SimpleCache(cacheDir, evictor, databaseProvider)
                    android.util.Log.d("VideoCache", "Cache initialized at ${cacheDir.absolutePath}")
                } catch (e: Exception) {
                    android.util.Log.e("VideoCache", "Failed to initialize cache: ${e.message}")
                    return null
                }
            }
            return simpleCache
        }
    }

    fun getCacheDataSourceFactory(context: Context): androidx.media3.datasource.DataSource.Factory {
        val cache = getInstance(context)
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        if (cache == null) return httpDataSourceFactory

        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}
