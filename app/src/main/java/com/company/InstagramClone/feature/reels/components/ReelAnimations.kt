package com.company.InstagramClone.feature.reels.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun DoubleTapHeartAnimation(
    modifier: Modifier = Modifier,
    trigger: Boolean,
    onAnimationEnd: () -> Unit
) {
    if (trigger) {
        val transitionState = remember { MutableTransitionState(false) }
        LaunchedEffect(Unit) {
            transitionState.targetState = true
            delay(1000)
            transitionState.targetState = false
            onAnimationEnd()
        }

        val transition = updateTransition(transitionState, label = "HeartTransition")
        
        val scale by transition.animateFloat(
            transitionSpec = {
                if (targetState) {
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                } else {
                    tween(durationMillis = 200)
                }
            },
            label = "Scale"
        ) { state -> if (state) 1.2f else 0f }

        val alpha by transition.animateFloat(
            transitionSpec = { tween(durationMillis = if (targetState) 100 else 400) },
            label = "Alpha"
        ) { state -> if (state) 1f else 0f }

        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White.copy(alpha = alpha),
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
            )
        }
    }
}
