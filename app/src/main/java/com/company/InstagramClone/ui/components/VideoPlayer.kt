package com.company.InstagramClone.ui.components

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.company.InstagramClone.utils.VideoCache
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    autoPlay: Boolean = true,
    shouldPlay: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isManualPaused by remember { mutableStateOf(false) }
    var isAppInBackground by remember { mutableStateOf(false) }
    var showOverlayIcon by remember { mutableStateOf<ImageVector?>(null) }
    
    // Lifecycle observer to handle background/foreground transitions
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                    isAppInBackground = true
                }
                Lifecycle.Event.ON_RESUME -> {
                    isAppInBackground = false
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Reset manual pause when the video is scrolled away
    LaunchedEffect(shouldPlay) {
        if (!shouldPlay) {
            isManualPaused = false
        }
    }

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
                playWhenReady = autoPlay && shouldPlay && !isManualPaused && !isAppInBackground
                
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

    LaunchedEffect(shouldPlay, isManualPaused, isAppInBackground) {
        exoPlayer.playWhenReady = shouldPlay && !isManualPaused && !isAppInBackground
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

    Box(
        modifier = modifier.clickable {
            isManualPaused = !isManualPaused
            showOverlayIcon = if (isManualPaused) Icons.Default.Pause else Icons.Default.PlayArrow
        },
        contentAlignment = Alignment.Center
    ) {
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

        // Play/Pause Overlay Icon
        AnimatedVisibility(
            visible = showOverlayIcon != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            showOverlayIcon?.let { icon ->
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                    )
                }
                
                LaunchedEffect(showOverlayIcon) {
                    delay(800)
                    showOverlayIcon = null
                }
            }
        }
    }
}
