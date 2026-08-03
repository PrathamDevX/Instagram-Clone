package com.company.InstagramClone.feature.story

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.model.StoryRecord
import com.company.InstagramClone.ui.components.VideoPlayer
import com.company.InstagramClone.ui.theme.InstagramSans
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StoryViewModel(
    private val socialRepository: SocialRepository = SocialRepository()
) : ViewModel() {
    private val _stories = MutableStateFlow<List<StoryRecord>>(emptyList())
    val stories = _stories.asStateFlow()

    fun fetchUserStories(userId: String) {
        viewModelScope.launch {
            socialRepository.getActiveStories()
                .onSuccess { allStories ->
                    _stories.value = allStories.filter { it.userId == userId }
                }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryViewerScreen(
    userId: String,
    navController: NavController,
    viewModel: StoryViewModel = viewModel()
) {
    val stories by viewModel.stories.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(userId) {
        viewModel.fetchUserStories(userId)
    }

    if (stories.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val currentStory = stories[currentIndex]

    LaunchedEffect(currentIndex) {
        progress = 0f
        val duration = 5000L // 5 seconds per story
        val steps = 100
        for (i in 1..steps) {
            delay(duration / steps)
            progress = i.toFloat() / steps
        }
        if (currentIndex < stories.size - 1) {
            currentIndex++
        } else {
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        if (offset.x < size.width / 3) {
                            if (currentIndex > 0) currentIndex--
                        } else {
                            if (currentIndex < stories.size - 1) currentIndex++
                            else navController.popBackStack()
                        }
                    }
                )
            }
    ) {
        // Media Content
        if (currentStory.mediaType == "video") {
            VideoPlayer(
                videoUrl = currentStory.mediaUrl,
                modifier = Modifier.fillMaxSize(),
                autoPlay = true
            )
        } else {
            GlideImage(
                model = currentStory.mediaUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Header & Progress
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .statusBarsPadding()
        ) {
            // Progress Bar segments
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stories.forEachIndexed { index, _ ->
                    val segmentProgress = when {
                        index < currentIndex -> 1f
                        index == currentIndex -> progress
                        else -> 0f
                    }
                    LinearProgressIndicator(
                        progress = { segmentProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Gray, shape = androidx.compose.foundation.shape.CircleShape)
                ) {
                    if (currentStory.profileImageUrl.isNotEmpty()) {
                        GlideImage(
                            model = currentStory.profileImageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = currentStory.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = InstagramSans
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        }
    }
}
