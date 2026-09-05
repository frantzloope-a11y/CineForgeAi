package com.example.ui.screens.image

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import com.example.ads.UnityAdsManager
import com.example.data.model.CreationEntity
import com.example.data.repository.GenerationStage
import com.example.ui.components.AspectRatioSelector
import com.example.ui.components.GenerationLoader
import com.example.ui.components.GradientButton
import com.example.ui.components.MediaCard
import com.example.ui.components.MediaDetailDialog
import com.example.ui.components.PromptEditor
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkCard
import com.example.ui.theme.ElectricIndigo
import com.example.ui.viewmodel.GenerationUiState
import com.example.ui.viewmodel.MainViewModel
import com.example.utils.PromptEnhancer

@Composable
fun TextToImageScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val genState by viewModel.imageGenState.collectAsState()

    var prompt by remember { mutableStateOf("") }
    var selectedRatio by remember { mutableStateOf("16:9") }
    var selectedCopies by remember { mutableIntStateOf(1) }
    var selectedCreationForDetail by remember { mutableStateOf<CreationEntity?>(null) }

    LaunchedEffect(Unit) {
        val incoming = viewModel.consumePrefilledPrompt()
        if (!incoming.isNullOrBlank()) {
            prompt = incoming
        }
    }

    val allRatios = listOf(
        "1:1", "16:9", "9:16", "4:3", "3:4",
        "2:1", "1:2", "3:2", "2:3", "4:5", "5:4"
    )

    val copyOptions = listOf(1, 2, 4, 8, 16, 50)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("text_to_image_screen")
    ) {
        when (val state = genState) {
            is GenerationUiState.Generating -> {
                GenerationLoader(
                    currentStage = state.stage,
                    title = "Synthesizing AI Artwork..."
                )
            }

            is GenerationUiState.Success, is GenerationUiState.BulkSuccess -> {
                val results = if (state is GenerationUiState.Success) {
                    listOf(state.creation)
                } else {
                    (state as GenerationUiState.BulkSuccess).creations
                }

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
                                onClick = { viewModel.resetImageGenState() },
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
                                    text = "Generated Artwork (${results.size})",
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
                        // Display Images
                        results.forEach { creation ->
                            MediaCard(
                                creation = creation,
                                onClick = { selectedCreationForDetail = creation },
                                onFavoriteToggle = {
                                    viewModel.toggleFavorite(creation.id, creation.isFavorite)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GradientButton(
                                text = "Regenerate",
                                onClick = {
                                    viewModel.generateTextToImage(prompt, selectedRatio, selectedCopies)
                                },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedButton(
                                onClick = { viewModel.resetImageGenState() },
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                            ) {
                                Text("New Prompt", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
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
                                    text = "Imagine → Create",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "High-definition AI image synthesis engine.",
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

                    // Prompt Box
                    item {
                        PromptEditor(
                            prompt = prompt,
                            onPromptChange = { prompt = it },
                            placeholder = "Describe anything you can imagine... e.g. An astronaut exploring an ancient bioluminescent crystalline cavern on Europa...",
                            onEnhancePrompt = {
                                if (prompt.isNotBlank()) {
                                    prompt = PromptEnhancer.enhance(prompt)
                                }
                            },
                            testTag = "text_to_image_prompt_input"
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Aspect Ratios (All 11 supported)
                    item {
                        AspectRatioSelector(
                            selectedRatio = selectedRatio,
                            onRatioSelected = { selectedRatio = it },
                            ratios = allRatios
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Copies Selector
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Number of Copies",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                copyOptions.forEach { copies ->
                                    val isSelected = copies == selectedCopies
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
                                            .clickable { selectedCopies = copies }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$copies",
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

                    // Main Action CTA: GENERATE ARTWORK (triggers Interstitial ad after every 2 creations)
                    item {
                        GradientButton(
                            text = "GENERATE ARTWORK ✦",
                            onClick = {
                                val activity = context as? Activity
                                val launchGen = {
                                    viewModel.generateTextToImage(
                                        prompt = prompt.ifBlank { "Futuristic architectural masterpiece surrounded by cascading waterfalls, golden hour, Octane render" },
                                        ratio = selectedRatio,
                                        copies = selectedCopies
                                    )
                                }

                                val shouldShowAd = viewModel.shouldShowInterstitialAd()
                                if (shouldShowAd && activity != null) {
                                    UnityAdsManager.showInterstitial(activity, onAdClosed = launchGen)
                                } else {
                                    launchGen()
                                }
                            },
                            height = 58.dp,
                            testTag = "generate_artwork_button"
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Rewarded Ad Option for 4x Variations
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    val activity = context as? Activity
                                    if (activity != null) {
                                        UnityAdsManager.showRewarded(
                                            activity = activity,
                                            onRewardEarned = {
                                                selectedCopies = 4
                                                android.widget.Toast.makeText(
                                                    context,
                                                    "✦ 4x Variations Batch Unlocked for Free!",
                                                    android.widget.Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 13.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Watch Ad to Unlock 4x Variations Batch",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Generate 4 images at once · Powered by Rewarded Ads",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }

    selectedCreationForDetail?.let { creation ->
        MediaDetailDialog(
            creation = creation,
            onDismiss = { selectedCreationForDetail = null },
            onDelete = {
                viewModel.deleteCreation(creation.id)
                selectedCreationForDetail = null
            },
            onFavoriteToggle = {
                viewModel.toggleFavorite(creation.id, creation.isFavorite)
                selectedCreationForDetail = creation.copy(isFavorite = !creation.isFavorite)
            },
            onRegenerate = { p ->
                prompt = p
                viewModel.generateTextToImage(p, selectedRatio, selectedCopies)
            }
        )
    }
}
