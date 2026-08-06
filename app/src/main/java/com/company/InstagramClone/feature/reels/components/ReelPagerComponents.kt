package com.company.InstagramClone.feature.reels.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.company.InstagramClone.R
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.ui.theme.InstagramSans
import java.util.Locale

@Composable
fun ReelActions(
    reel: ReelRecord,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 16.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ReelActionItem(
            icon = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            label = formatCount(reel.likesCount),
            onClick = onLikeClick,
            tint = if (reel.isLiked) Color.Red else Color.White
        )
        ReelActionItem(
            icon = painterResource(id = R.drawable.chat),
            label = formatCount(reel.commentsCount),
            onClick = onCommentClick
        )
        ReelActionItem(
            icon = painterResource(id = R.drawable.send),
            label = "",
            onClick = onShareClick
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Music Disc Placeholder
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
        )
    }
}

@Composable
fun ReelActionItem(
    icon: Any,
    label: String,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clickable { onClick() }
    ) {
        when (icon) {
            is androidx.compose.ui.graphics.vector.ImageVector -> Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
            is androidx.compose.ui.graphics.painter.Painter -> Icon(
                painter = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(26.dp)
            )
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InstagramSans
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReelDescription(
    reel: ReelRecord,
    onFollowClick: () -> Unit,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onUserClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                if (reel.profileImageUrl.isNotEmpty()) {
                    GlideImage(
                        model = reel.profileImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = reel.username,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = InstagramSans
            )
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                onClick = onFollowClick,
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp),
                border = borderStroke(1.dp, Color.White)
            ) {
                Text(
                    text = "Follow",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InstagramSans
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = reel.caption,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            fontFamily = InstagramSans
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.MusicNote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Scrolling Marquee Placeholder
            Text(
                text = "${reel.username} · Original audio",
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 1,
                fontFamily = InstagramSans
            )
        }
    }
}

private fun formatCount(count: Int): String {
    return if (count >= 1000) "${String.format(Locale.getDefault(), "%.1f", count / 1000f)}K" else count.toString()
}

@Composable
private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = 
    androidx.compose.foundation.BorderStroke(width, color)
