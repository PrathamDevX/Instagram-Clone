package com.company.InstagramClone.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

@OptIn(UnstableApi::class)
class PlayerPoolManager(private val context: Context) {
    private val poolSize = 3
    private val playerPool = mutableListOf<ExoPlayer>()
    private val activePlayers = mutableMapOf<String, ExoPlayer>()

    init {
        repeat(poolSize) {
            playerPool.add(createPlayer())
        }
    }

    private fun createPlayer(): ExoPlayer {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(15000, 50000, 1000, 2000)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoCache.getCacheDataSourceFactory(context)))
            .setLoadControl(loadControl)
            .build().apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                repeatMode = Player.REPEAT_MODE_ALL
            }
    }

    fun acquirePlayer(id: String): ExoPlayer? {
        if (activePlayers.containsKey(id)) return activePlayers[id]
        
        if (playerPool.isNotEmpty()) {
            val player = playerPool.removeAt(0)
            activePlayers[id] = player
            return player
        }
        
        // If pool is empty, steal the oldest active player (LRU style could be better, but keeping it simple)
        val firstKey = activePlayers.keys.firstOrNull()
        if (firstKey != null) {
            val player = activePlayers.remove(firstKey)
            if (player != null) {
                player.stop()
                player.clearMediaItems()
                activePlayers[id] = player
                return player
            }
        }
        
        return null
    }

    fun releasePlayer(id: String) {
        val player = activePlayers.remove(id)
        if (player != null) {
            player.stop()
            player.clearMediaItems()
            playerPool.add(player)
        }
    }

    fun releaseAll() {
        activePlayers.values.forEach { it.release() }
        activePlayers.clear()
        playerPool.forEach { it.release() }
        playerPool.clear()
    }
    
    companion object {
        @Volatile
        private var instance: PlayerPoolManager? = null
        
        fun getInstance(context: Context): PlayerPoolManager {
            return instance ?: synchronized(this) {
                instance ?: PlayerPoolManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
