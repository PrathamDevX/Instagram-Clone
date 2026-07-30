package com.company.InstagramClone.feature.create

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.create.components.*
import com.company.InstagramClone.ui.theme.InstagramBlack

@Composable
fun CreateMediaScreen(
    navController: NavController,
    viewModel: CreateMediaViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var selectedMode by remember { mutableStateOf(CreateMode.STORY) }
    
    var hasCameraPermission by remember { mutableStateOf(false) }
    var hasAudioPermission by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: false
        hasAudioPermission = permissions[Manifest.permission.RECORD_AUDIO] ?: false
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onMediaSelected(context, uri, selectedMode)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
    }

    if (!hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize().background(InstagramBlack), contentAlignment = Alignment.Center) {
            Text("Camera permission required", color = Color.White)
        }
        return
    }

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashEnabled by remember { mutableStateOf(false) }
    
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    
    val imageCapture = remember { ImageCapture.Builder().build() }
    val recorder = remember { Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST)).build() }
    val videoCapture = remember { VideoCapture.withOutput(recorder) }
    
    val isRecording by viewModel.isRecording.collectAsState()
    val createState by viewModel.createState.collectAsState()

    LaunchedEffect(lensFacing) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                videoCapture
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Camera bind failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(createState) {
        when (createState) {
            is CreateState.Success -> {
                Toast.makeText(context, (createState as CreateState.Success).message, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is CreateState.Error -> {
                Toast.makeText(context, (createState as CreateState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Camera Preview Section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(InstagramBlack)
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            // Top Bar Overlay
            CameraTopBar(
                isFlashOn = flashEnabled,
                onFlashToggle = { flashEnabled = !flashEnabled },
                onClose = { navController.popBackStack() }
            )

            // Side Menu Overlay
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 80.dp),
                contentAlignment = Alignment.TopStart
            ) {
                CameraSideMenu()
            }
        }

        // Bottom Control Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CaptureControls(
                onTap = {
                    if (selectedMode != CreateMode.REEL) {
                        viewModel.capturePhoto(context, imageCapture, CameraSelector.DEFAULT_BACK_CAMERA, selectedMode)
                    } else {
                        if (isRecording) viewModel.stopVideoRecording()
                        else viewModel.startVideoRecording(context, videoCapture, selectedMode)
                    }
                },
                onLongPressStart = {
                    viewModel.startVideoRecording(context, videoCapture, selectedMode)
                },
                onLongPressEnd = {
                    viewModel.stopVideoRecording()
                },
                onSwitchCamera = {
                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                        CameraSelector.LENS_FACING_FRONT
                    } else {
                        CameraSelector.LENS_FACING_BACK
                    }
                },
                onGalleryClick = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                },
                isRecording = isRecording
            )

            ModeSelector(
                selectedMode = selectedMode,
                onModeSelected = { selectedMode = it }
            )
        }
    }

    if (createState is CreateState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}
