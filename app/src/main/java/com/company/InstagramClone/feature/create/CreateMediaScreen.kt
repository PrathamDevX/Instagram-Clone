package com.company.InstagramClone.feature.create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
    
    var hasCameraPermission by remember { mutableStateOf(false) }
    var hasAudioPermission by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: false
        hasAudioPermission = permissions[Manifest.permission.RECORD_AUDIO] ?: false
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
    var selectedMode by remember { mutableStateOf(CreateMode.STORY) }
    
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

    Box(modifier = Modifier.fillMaxSize().background(InstagramBlack)) {
        // Camera Preview
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // UI Overlays
        Column(modifier = Modifier.fillMaxSize()) {
            CameraTopBar(
                isFlashOn = flashEnabled,
                onFlashToggle = { flashEnabled = !flashEnabled },
                onClose = { navController.popBackStack() }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                CameraSideMenu()
                Spacer(modifier = Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CaptureControls(
                    onCapture = {
                        if (selectedMode == CreateMode.REEL) {
                            viewModel.toggleVideoRecording(context, videoCapture, selectedMode)
                        } else {
                            viewModel.capturePhoto(context, imageCapture, CameraSelector.DEFAULT_BACK_CAMERA, selectedMode)
                        }
                    },
                    onSwitchCamera = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
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
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
