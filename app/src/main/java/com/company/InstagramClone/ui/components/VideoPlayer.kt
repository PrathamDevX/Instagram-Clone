package com.company.InstagramClone.ui.components

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.company.InstagramClone.utils.VideoCache

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    autoPlay: Boolean = true
) {
    val context = LocalContext.current
    
    // Custom LoadControl for smoother buffering and reduced "wavy" quality
    val loadControl = remember {
        DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                30000, // Min buffer 30s
                50000, // Max buffer 50s
                2500,  // Buffer for playback 2.5s
                5000   // Buffer for playback after rebuffer 5s
            )
            .build()
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoCache.getCacheDataSourceFactory(context)))
            .setLoadControl(loadControl)
            .build().apply {
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                repeatMode = Player.REPEAT_MODE_ALL
                playWhenReady = autoPlay
                
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        android.util.Log.e("VideoPlayer", "ExoPlayer Error: ${error.message}", error)
                    }
                    
                    override fun onPlaybackStateChanged(state: Int) {
                        val stateName = when(state) {
                            Player.STATE_BUFFERING -> "BUFFERING"
                            Player.STATE_READY -> "READY"
                            Player.STATE_ENDED -> "ENDED"
                            Player.STATE_IDLE -> "IDLE"
                            else -> "UNKNOWN"
                        }
                        android.util.Log.d("VideoPlayer", "Playback State: $stateName")
                    }
                })
            }
    }

    var isPlayerMuted by remember { mutableStateOf(isMuted) }

    LaunchedEffect(videoUrl) {
        android.util.Log.d("VideoPlayer", "Loading video: $videoUrl")
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
        exoPlayer.prepare()
    }

    LaunchedEffect(isPlayerMuted) {
        exoPlayer.volume = if (isPlayerMuted) 0f else 1f
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = modifier.clickable { isPlayerMuted = !isPlayerMuted }) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    useController = false
                    resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
