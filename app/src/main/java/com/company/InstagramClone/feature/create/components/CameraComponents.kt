package com.company.InstagramClone.feature.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            .statusBarsPadding()
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Color.White, fontSize = 12.sp, fontFamily = InstagramSans)
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
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CreateMode.values().forEach { mode ->
            Text(
                text = mode.name,
                color = if (selectedMode == mode) Color.White else Color.Gray,
                fontWeight = if (selectedMode == mode) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onModeSelected(mode) },
                fontFamily = InstagramSans
            )
        }
    }
}

@Composable
fun CaptureControls(
    onCapture: () -> Unit,
    onSwitchCamera: () -> Unit,
    isRecording: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gallery Preview Placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
        )

        // Shutter Button
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(5.dp, Color.White, CircleShape)
                .padding(6.dp)
                .clip(CircleShape)
                .background(if (isRecording) Color.Red else Color.White)
                .clickable { onCapture() }
        )

        // Switch Camera
        IconButton(onClick = onSwitchCamera) {
            Icon(
                imageVector = Icons.Outlined.Sync,
                contentDescription = "Switch Camera",
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
        }
    }
}

enum class CreateMode {
    POST, STORY, REEL, LIVE
}
