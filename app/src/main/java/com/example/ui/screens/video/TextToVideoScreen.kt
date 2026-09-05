package com.example.ui.screens.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AspectRatioSelector
import com.example.ui.components.GenerationLoader
import com.example.ui.components.GradientButton
import com.example.ui.components.PromptEditor
import com.example.ui.components.StyleSelector
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.utils.PromptEnhancer
import com.example.ui.viewmodel.GenerationUiState
import com.example.ui.viewmodel.MainViewModel
import com.example.utils.MediaDownloader
import com.example.utils.ShareUtils
import okhttp3.OkHttpClient

@Composable
fun TextToVideoScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val genState by viewModel.videoGenState.collectAsState()

    var prompt by remember { mutableStateOf("") }
    var negativePrompt by remember { mutableStateOf("") }
    var selectedRatio by remember { mutableStateOf("9:16") }
    var selectedDuration by remember { mutableIntStateOf(5) }
    var selectedStyle by remember { mutableStateOf("Cinematic") }
    var selectedCameraMotion by remember { mutableStateOf("Slow Push In") }
    var selectedQuality by remember { mutableStateOf("High") }

    // Check for prefilled prompt from PromptLab/Library
    LaunchedEffect(Unit) {
        val incoming = viewModel.consumePrefilledPrompt()
        if (!incoming.isNullOrBlank()) {
            prompt = incoming
        }
    }

    val styles = listOf(
        "Cinematic", "Realistic", "Anime", "3D", "Fantasy",
        "Product Commercial", "Documentary", "Vlog", "Cyberpunk", "Minimal"
    )

    val cameraMotions = listOf(
        "Static", "Slow Push In", "Pull Out", "Pan Left", "Pan Right",
        "Tilt", "Drone Shot", "Tracking Shot", "Orbit", "Handheld"
    )

    val durations = listOf(5, 10, 15, 20)
    val qualities = listOf("Standard", "High", "Ultra")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("text_to_video_screen")
    ) {
        when (val state = genState) {
            is GenerationUiState.Generating -> {
                GenerationLoader(
                    currentStage = state.stage,
                    title = state.title
                )
            }

            is GenerationUiState.Success -> {
                // Video Result Screen
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
                                onClick = { viewModel.resetVideoGenState() },
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
                                    text = "Generated Video",
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
                        // Video Player Card
                        VideoPlayerCard(
                            videoUrl = state.creation.mediaUrl,
                            prompt = state.creation.prompt,
                            ratio = state.creation.ratio,
                            autoPlay = true,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "generated_video_player"
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        // Action Buttons: Regenerate & Create Another
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GradientButton(
                                text = "Regenerate",
                                onClick = {
                                    viewModel.generateTextToVideo(
                                        prompt = prompt,
                                        ratio = selectedRatio,
                                        duration = selectedDuration,
                                        style = selectedStyle,
                                        cameraMotion = selectedCameraMotion,
                                        quality = selectedQuality,
                                        negativePrompt = negativePrompt.ifBlank { null }
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )

                            OutlinedButton(
                                onClick = { viewModel.resetVideoGenState() },
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                            ) {
                                Text(
                                    text = "New Video",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        // Prompt details card
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Prompt",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyanAccent
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = state.creation.prompt,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Ratio", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(state.creation.ratio, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Duration", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("${state.creation.duration}s", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Style", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(state.creation.style ?: "Cinematic", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                // Composer Form
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
                ) {
                    // Header with back button
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
                                    text = "Text to Video",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Describe your scene and let AI bring it to life.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Error Banner if present
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
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Error",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = err.message,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // Large Prompt Composer
                    item {
                        PromptEditor(
                            prompt = prompt,
                            onPromptChange = { prompt = it },
                            placeholder = "Describe your video... e.g. A cinematic drone shot flying over snowy mountains at sunset, 35mm lens, golden light...",
                            onEnhancePrompt = {
                                if (prompt.isNotBlank()) {
                                    prompt = com.example.utils.PromptEnhancer.enhance(prompt)
                                }
                            },
                            negativePrompt = negativePrompt,
                            onNegativePromptChange = { negativePrompt = it },
                            minHeight = 130,
                            testTag = "text_to_video_prompt_input"
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Aspect Ratio Selector
                    item {
                        AspectRatioSelector(
                            selectedRatio = selectedRatio,
                            onRatioSelected = { selectedRatio = it },
                            ratios = listOf("9:16", "16:9", "1:1")
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Duration Selector
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
                                            .testTag("duration_${duration}s")
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

                    // Style Selector
                    item {
                        StyleSelector(
                            title = "Visual Style",
                            options = styles,
                            selectedOption = selectedStyle,
                            onOptionSelected = { selectedStyle = it },
                            testTagPrefix = "style"
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Camera Motion Selector
                    item {
                        StyleSelector(
                            title = "Camera Motion",
                            options = cameraMotions,
                            selectedOption = selectedCameraMotion,
                            onOptionSelected = { selectedCameraMotion = it },
                            testTagPrefix = "camera"
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Quality Selector
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Render Quality",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                qualities.forEach { q ->
                                    val isSelected = q == selectedQuality
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
                                            .clickable { selectedQuality = q }
                                            .testTag("quality_$q")
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = q,
                                                color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    // Main Action CTA: GENERATE VIDEO
                    item {
                        GradientButton(
                            text = "GENERATE VIDEO ✦",
                            onClick = {
                                viewModel.generateTextToVideo(
                                    prompt = prompt.ifBlank { "Cinematic aerial view of futuristic city illuminated by neon lights and flying vehicles at dusk" },
                                    ratio = selectedRatio,
                                    duration = selectedDuration,
                                    style = selectedStyle,
                                    cameraMotion = selectedCameraMotion,
                                    quality = selectedQuality,
                                    negativePrompt = negativePrompt.ifBlank { null }
                                )
                            },
                            enabled = true,
                            height = 58.dp,
                            testTag = "generate_video_button"
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
