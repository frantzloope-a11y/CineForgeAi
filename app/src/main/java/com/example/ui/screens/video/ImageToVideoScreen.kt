package com.example.ui.screens.video

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.AspectRatioSelector
import com.example.ui.components.GenerationLoader
import com.example.ui.components.GradientButton
import com.example.ui.components.StyleSelector
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkCard
import com.example.ui.theme.ElectricIndigo
import com.example.ui.viewmodel.GenerationUiState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ImageToVideoScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val genState by viewModel.ptvGenState.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var motionPrompt by remember { mutableStateOf("") }
    var selectedRatio by remember { mutableStateOf("9:16") }
    var selectedDuration by remember { mutableIntStateOf(5) }
    var selectedIntensity by remember { mutableStateOf("Balanced") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val quickMotionChips = listOf(
        "Camera slowly moves forward",
        "Hair and clothes move naturally in the wind",
        "Ocean waves move realistically",
        "Character walks toward camera",
        "Dynamic 360 orbit around subject",
        "Subtle cinematic breathing motion"
    )

    val intensities = listOf("Subtle", "Balanced", "Dynamic", "Wild")
    val durations = listOf(5, 10, 15, 20)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("image_to_video_screen")
    ) {
        when (val state = genState) {
            is GenerationUiState.Generating -> {
                GenerationLoader(
                    currentStage = state.stage,
                    title = state.title
                )
            }

            is GenerationUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 90.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { viewModel.resetPtvGenState() },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Animated Video",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Saved automatically to Creations",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyanAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        VideoPlayerCard(
                            videoUrl = state.creation.mediaUrl,
                            prompt = state.creation.prompt,
                            ratio = state.creation.ratio,
                            autoPlay = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GradientButton(
                                text = "Animate Again",
                                onClick = {
                                    selectedImageUri?.let { uri ->
                                        viewModel.animateImageToVideo(
                                            imageUri = uri,
                                            motionPrompt = motionPrompt,
                                            ratio = selectedRatio,
                                            duration = selectedDuration,
                                            motionIntensity = selectedIntensity
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedButton(
                                onClick = { viewModel.resetPtvGenState() },
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                            ) {
                                Text(
                                    text = "New Image",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Animate Your Image",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Turn still photos into moving cinematic video.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    if (genState is GenerationUiState.Error) {
                        item {
                            val err = genState as GenerationUiState.Error
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0x33EF4444),
                                border = BorderStroke(1.dp, Color(0xFFEF4444)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFEF4444))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(err.message, color = Color.White, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    // UPLOAD AREA
                    item {
                        if (selectedImageUri == null) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.5.dp, CyanAccent.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .testTag("upload_image_area")
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(ElectricIndigo.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Upload",
                                            tint = CyanAccent,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Drop or select an image here",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "JPG, PNG or WebP · Up to 10MB",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        } else {
                            // Image Preview Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(DarkCard)
                            ) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Uploaded Image Preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                IconButton(
                                    onClick = { selectedImageUri = null },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.7f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Image",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // MOTION PROMPT
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Motion Prompt",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = motionPrompt,
                                onValueChange = { motionPrompt = it },
                                placeholder = {
                                    Text("Describe how the image should move...", fontSize = 14.sp)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("motion_prompt_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                maxLines = 3
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick chips
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 2.dp)
                            ) {
                                items(quickMotionChips) { chip ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { motionPrompt = chip }
                                    ) {
                                        Text(
                                            text = chip,
                                            fontSize = 12.sp,
                                            color = CyanAccent,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Aspect Ratio
                    item {
                        AspectRatioSelector(
                            selectedRatio = selectedRatio,
                            onRatioSelected = { selectedRatio = it },
                            ratios = listOf("9:16", "16:9", "1:1")
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Duration
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Duration",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                durations.forEach { duration ->
                                    val isSelected = duration == selectedDuration
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) ElectricIndigo.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceVariant,
                                        border = BorderStroke(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedDuration = duration }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${duration}s",
                                                color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Motion Intensity
                    item {
                        StyleSelector(
                            title = "Motion Intensity",
                            options = intensities,
                            selectedOption = selectedIntensity,
                            onOptionSelected = { selectedIntensity = it }
                        )

                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    // Main Action CTA: ANIMATE IMAGE
                    item {
                        GradientButton(
                            text = "ANIMATE IMAGE ✦",
                            onClick = {
                                selectedImageUri?.let { uri ->
                                    viewModel.animateImageToVideo(
                                        imageUri = uri,
                                        motionPrompt = motionPrompt,
                                        ratio = selectedRatio,
                                        duration = selectedDuration,
                                        motionIntensity = selectedIntensity
                                    )
                                }
                            },
                            enabled = selectedImageUri != null,
                            height = 58.dp,
                            testTag = "animate_image_button"
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
