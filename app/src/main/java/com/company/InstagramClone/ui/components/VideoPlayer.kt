package com.company.InstagramClone.ui.components

import android.net.Uri
import android.view.LayoutInflater
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
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.company.InstagramClone.R
import com.company.InstagramClone.utils.PlayerPoolManager
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
    
    // Lifecycle observer
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> isAppInBackground = true
                Lifecycle.Event.ON_RESUME -> isAppInBackground = false
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(shouldPlay) {
        if (!shouldPlay) isManualPaused = false
    }

    // Single Player from Pool
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    val poolManager = remember { PlayerPoolManager.getInstance(context) }

    LaunchedEffect(shouldPlay, isAppInBackground, videoUrl) {
        if (shouldPlay && !isAppInBackground) {
            if (exoPlayer == null) {
                val player = poolManager.acquirePlayer(videoUrl)
                if (player != null) {
                    player.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                    player.prepare()
                    exoPlayer = player
                }
            }
            exoPlayer?.playWhenReady = !isManualPaused
        } else {
            if (exoPlayer != null) {
                poolManager.releasePlayer(videoUrl)
                exoPlayer = null
            }
        }
    }

    DisposableEffect(videoUrl) {
        onDispose {
            poolManager.releasePlayer(videoUrl)
            exoPlayer = null
        }
    }

    var isPlayerMuted by remember { mutableStateOf(isMuted) }
    LaunchedEffect(isPlayerMuted, exoPlayer) {
        exoPlayer?.volume = if (isPlayerMuted) 0f else 1f
    }

    Box(
        modifier = modifier.clickable {
            isManualPaused = !isManualPaused
            showOverlayIcon = if (isManualPaused) Icons.Default.Pause else Icons.Default.PlayArrow
        },
        contentAlignment = Alignment.Center
    ) {
        if (exoPlayer != null) {
            AndroidView(
                factory = {
                    val view = LayoutInflater.from(context).inflate(R.layout.texture_player_view, null) as PlayerView
                    view.player = exoPlayer
                    view
                },
                update = { view ->
                    view.player = exoPlayer
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay Icon
        AnimatedVisibility(
            visible = showOverlayIcon != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            showOverlayIcon?.let { icon ->
                Box(
                    modifier = Modifier.size(60.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(35.dp))
                }
                LaunchedEffect(showOverlayIcon) {
                    delay(800)
                    showOverlayIcon = null
                }
            }
        }
    }
}
