package com.company.InstagramClone.feature.create

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.MediaRepository
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.model.PostRecord
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.data.model.StoryRecord
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

sealed class CreateState {
    object Idle : CreateState()
    object Loading : CreateState()
    data class Success(val message: String) : CreateState()
    data class Error(val message: String) : CreateState()
}

class CreateMediaViewModel(
    private val repository: com.company.InstagramClone.data.AuthRepository = com.company.InstagramClone.data.FirebaseAuthRepository(),
    private val mediaRepository: MediaRepository = MediaRepository(),
    private val socialRepository: SocialRepository = SocialRepository()
) : ViewModel() {

    private val _createState = MutableStateFlow<CreateState>(CreateState.Idle)
    val createState = _createState.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private var activeRecording: Recording? = null

    fun capturePhoto(
        context: Context,
        imageCapture: ImageCapture,
        cameraSelector: CameraSelector,
        mode: com.company.InstagramClone.feature.create.components.CreateMode
    ) {
        _createState.value = CreateState.Loading
        val photoFile = File(
            context.cacheDir,
            SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(photoFile)
                    uploadAndSave(uri, mode)
                }

                override fun onError(exception: ImageCaptureException) {
                    _createState.value = CreateState.Error("Capture failed: ${exception.message}")
                }
            }
        )
    }

    fun startVideoRecording(
        context: Context,
        videoCapture: VideoCapture<Recorder>,
        mode: com.company.InstagramClone.feature.create.components.CreateMode
    ) {
        if (activeRecording != null) return

        val name = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(System.currentTimeMillis())
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(context.contentResolver, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        activeRecording = videoCapture.output
            .prepareRecording(context, mediaStoreOutputOptions)
            .withAudioEnabled()
            .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> _isRecording.value = true
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val uri = recordEvent.outputResults.outputUri
                            uploadAndSave(uri, mode)
                        } else {
                            _createState.value = CreateState.Error("Video error: ${recordEvent.error}")
                        }
                        _isRecording.value = false
                    }
                }
            }
    }

    fun stopVideoRecording() {
        activeRecording?.stop()
        activeRecording = null
        _isRecording.value = false
    }

    fun onMediaSelected(
        context: Context,
        uri: Uri,
        mode: com.company.InstagramClone.feature.create.components.CreateMode
    ) {
        val type = context.contentResolver.getType(uri)
        val isVideo = type?.startsWith("video") == true
        uploadAndSave(uri, mode, isVideo)
    }

    private fun uploadAndSave(
        uri: Uri,
        mode: com.company.InstagramClone.feature.create.components.CreateMode,
        isVideo: Boolean = false
    ) {
        viewModelScope.launch {
            _createState.value = CreateState.Loading
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                _createState.value = CreateState.Error("User not logged in")
                return@launch
            }

            val profileResult = repository.getUserProfile()
            val profile = profileResult.getOrNull()

            val uploadResult = if (isVideo || mode == com.company.InstagramClone.feature.create.components.CreateMode.REEL) {
                mediaRepository.uploadVideo(uri)
            } else {
                mediaRepository.uploadImage(uri)
            }

            uploadResult.onSuccess { url ->
                val saveResult = when (mode) {
                    com.company.InstagramClone.feature.create.components.CreateMode.STORY -> {
                        socialRepository.saveStory(StoryRecord(
                            userId = user.uid,
                            username = profile?.username ?: "Anonymous",
                            profileImageUrl = profile?.profileImageUrl ?: "",
                            mediaUrl = url,
                            mediaType = if (isVideo) "video" else "image"
                        ))
                    }
                    com.company.InstagramClone.feature.create.components.CreateMode.POST -> {
                        socialRepository.savePost(PostRecord(
                            userId = user.uid,
                            username = profile?.username ?: "Anonymous",
                            profileImageUrl = profile?.profileImageUrl ?: "",
                            mediaUrls = listOf(url)
                        ))
                    }
                    com.company.InstagramClone.feature.create.components.CreateMode.REEL -> {
                        socialRepository.saveReel(ReelRecord(
                            userId = user.uid,
                            username = profile?.username ?: "Anonymous",
                            profileImageUrl = profile?.profileImageUrl ?: "",
                            videoUrl = url
                        ))
                    }
                    else -> Result.failure(Exception("Unsupported mode"))
                }

                saveResult.onSuccess {
                    _createState.value = CreateState.Success("Upload complete!")
                }.onFailure {
                    _createState.value = CreateState.Error("Database failed: ${it.message}")
                }
            }.onFailure {
                _createState.value = CreateState.Error("Upload failed: ${it.message}")
            }
        }
    }
}
