package com.company.InstagramClone.feature.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.InstagramClone.ui.theme.InstagramSans

@Composable
fun CameraTopBar(
    isFlashOn: Boolean,
    onFlashToggle: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(30.dp))
        }

        IconButton(onClick = onFlashToggle) {
            Icon(
                imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = "Flash",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun CameraSideMenu() {
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
            .width(60.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SideMenuItem(icon = Icons.Outlined.AutoAwesome, label = "Create")
        SideMenuItem(icon = Icons.Outlined.AllInclusive, label = "Boomerang")
        SideMenuItem(icon = Icons.Outlined.GridView, label = "Layout")
        
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SideMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(28.dp))
        // Label is usually hidden in main UI, only shown when expanded
    }
}

@Composable
fun ModeSelector(
    selectedMode: CreateMode,
    onModeSelected: (CreateMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CreateMode.entries.forEach { mode ->
            Text(
                text = mode.name,
                color = if (selectedMode == mode) Color.White else Color.Gray,
                fontWeight = if (selectedMode == mode) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .clickable { onModeSelected(mode) },
                fontFamily = InstagramSans
            )
        }
    }
}

@Composable
fun CaptureControls(
    onTap: () -> Unit,
    onLongPressStart: () -> Unit,
    onLongPressEnd: () -> Unit,
    onSwitchCamera: () -> Unit,
    onGalleryClick: () -> Unit,
    isRecording: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gallery Preview
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .background(Color.DarkGray)
                    .clickable { onGalleryClick() }
            )
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }

        // Main Shutter Button
        Box(
            modifier = Modifier
                .size(85.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onTap() },
                        onLongPress = { onLongPressStart() },
                        onPress = {
                            val pressSuccess = try {
                                awaitRelease()
                                true
                            } catch (c: Exception) {
                                false
                            }
                            if (pressSuccess) {
                                onLongPressEnd()
                            }
                        }
                    )
                }
                .border(5.dp, Color.White, CircleShape)
                .padding(6.dp)
                .clip(CircleShape)
                .background(if (isRecording) Color.Red else Color.White)
        )

        // Switch Camera and Zoom Icons
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Outlined.RemoveCircleOutline, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onSwitchCamera) {
                Icon(
                    imageVector = Icons.Outlined.Sync,
                    contentDescription = "Switch Camera",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Outlined.AddCircleOutline, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}

enum class CreateMode {
    POST, STORY, REEL, LIVE
}
