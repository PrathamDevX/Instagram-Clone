package com.company.InstagramClone.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

@OptIn(UnstableApi::class)
class PlayerPoolManager private constructor(private val context: Context) {
    private val poolSize = 4
    private val playerPool = mutableListOf<ExoPlayer>()
    private val activePlayers = mutableMapOf<String, ExoPlayer>()

    init {
        repeat(poolSize) {
            playerPool.add(createPlayer())
        }
    }

    private fun createPlayer(): ExoPlayer {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                15000, // minBufferMs
                50000, // maxBufferMs
                1000,  // bufferForPlaybackMs
                2000   // bufferForPlaybackAfterRebufferMs
            )
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
        
        // LRU-like stealing: if pool is empty, take the first one that is NOT the currently playing id
        // This is a simple implementation; ideally we'd track last access time.
        val targetKey = activePlayers.keys.firstOrNull { it != id }
        if (targetKey != null) {
            val player = activePlayers.remove(targetKey)
            if (player != null) {
                player.stop()
                player.clearMediaItems()
                activePlayers[id] = player
                return player
            }
        }
        
        return null
    }

    fun prewarm(id: String, url: String) {
        if (activePlayers.containsKey(id)) return
        
        val player = acquirePlayer(id)
        if (player != null) {
            player.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            player.prepare()
            player.playWhenReady = false // Prepare but don't play
        }
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
