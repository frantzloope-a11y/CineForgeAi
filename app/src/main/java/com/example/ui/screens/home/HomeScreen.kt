package com.example.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ThemeMode
import com.example.data.model.CreationEntity
import com.example.ui.components.EmptyState
import com.example.ui.components.GradientButton
import com.example.ui.components.MediaCard
import com.example.ui.components.MediaDetailDialog
import com.example.ui.components.SectionHeader
import com.example.ui.components.ToolCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.AmberBright
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.HeroCardGradient
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeDeep
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.RoseGlow
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.TextTertiaryDark
import com.example.ui.theme.VioletGlow
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val recentCreations by viewModel.recentCreations.collectAsState()
    var selectedCreationForDetail by remember { mutableStateOf<CreationEntity?>(null) }

    // Subtle animated glow pulse
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val glowShift by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_glow_shift"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp)
            .testTag("home_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // TOP APP BAR: Brand, Theme Toggle, Settings
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(AmberPrimary, OrangeDeep)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CF",
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "CineForge AI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Actions: Search/Theme toggle & Settings
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (themeMode == ThemeMode.DARK) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (themeMode == ThemeMode.DARK) AmberPrimary else CyanAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { onNavigate(Screen.Settings.route) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // HERO CARD: "Turn Words Into Worlds" (Sophisticated Dark)
        item {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, Color(0x33F59E0B)),
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_video_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HeroCardGradient)
                        .padding(22.dp)
                ) {
                    Column {
                        // Badge
                        Text(
                            text = "HERO FEATURE",
                            color = AmberPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Turn Words",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = "Into Worlds.",
                            style = MaterialTheme.typography.displayMedium,
                            color = AmberBright,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Generate cinematic AI videos from simple prompts in seconds.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GradientButton(
                                text = "Create Video →",
                                onClick = { onNavigate(Screen.TextToVideo.route) },
                                gradient = PrimaryGradient,
                                textColor = Color.Black,
                                modifier = Modifier.weight(1.1f),
                                testTag = "hero_create_video_button"
                            )

                            OutlinedButton(
                                onClick = { onNavigate(Screen.Explore.route) },
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(54.dp)
                            ) {
                                Text(
                                    text = "Explore",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
        }

        // QUICK TOOLS GRID (Sophisticated Dark)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Tools",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondaryDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "VIEW ALL",
                    color = AmberPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.clickable { onNavigate(Screen.Explore.route) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Text to Image
                QuickToolGridItem(
                    title = "Text to\nImage",
                    icon = Icons.Default.Image,
                    tint = CyanAccent,
                    bgColor = CyanAccent.copy(alpha = 0.12f),
                    onClick = { onNavigate(Screen.TextToImage.route) },
                    modifier = Modifier.weight(1f)
                )

                // Image to Video
                QuickToolGridItem(
                    title = "Image to\nVideo",
                    icon = Icons.Default.MovieCreation,
                    tint = VioletGlow,
                    bgColor = VioletGlow.copy(alpha = 0.12f),
                    onClick = { onNavigate(Screen.ImageToVideo.route) },
                    modifier = Modifier.weight(1f)
                )

                // Image to Image
                QuickToolGridItem(
                    title = "Image to\nImage",
                    icon = Icons.Default.Transform,
                    tint = EmeraldGlow,
                    bgColor = EmeraldGlow.copy(alpha = 0.12f),
                    onClick = { onNavigate(Screen.ImageToImage.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // QUICK ACCESS CHIPS: PromptLab, Library, Free Tools
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                item {
                    QuickChip(
                        title = "PromptLab AI",
                        icon = Icons.Default.Psychology,
                        tint = AmberPrimary,
                        onClick = { onNavigate(Screen.PromptLab.route) }
                    )
                }
                item {
                    QuickChip(
                        title = "Prompt Library",
                        icon = Icons.Default.AutoAwesome,
                        tint = OrangeAccent,
                        onClick = { onNavigate(Screen.PromptLibrary.route) }
                    )
                }
                item {
                    QuickChip(
                        title = "Free AI Tools",
                        icon = Icons.Default.MovieCreation,
                        tint = CyanAccent,
                        onClick = { onNavigate(Screen.FreeTools.route) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))
        }

        // PRIMARY STUDIO TOOLS SECTION
        item {
            SectionHeader(
                title = "Creative Studio",
                subtitle = "Select an AI creation modality to begin"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Hero Tool: Text to Video
            ToolCard(
                title = "Text to Video",
                subtitle = "Turn your imagination into cinematic motion.",
                icon = Icons.Default.Videocam,
                isHero = true,
                badgeText = "Hero Feature",
                onClick = { onNavigate(Screen.TextToVideo.route) },
                testTag = "home_text_to_video_card"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Tools
            ToolCard(
                title = "Text to Image",
                subtitle = "Generate high-resolution artwork and renders.",
                icon = Icons.Default.Image,
                onClick = { onNavigate(Screen.TextToImage.route) },
                testTag = "home_text_to_image_card"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ToolCard(
                title = "Image to Video",
                subtitle = "Animate still photography with fluid physics.",
                icon = Icons.Default.MovieCreation,
                onClick = { onNavigate(Screen.ImageToVideo.route) },
                testTag = "home_image_to_video_card"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ToolCard(
                title = "Image to Image",
                subtitle = "Transform styles, anime conversion, and cinematic looks.",
                icon = Icons.Default.Transform,
                onClick = { onNavigate(Screen.ImageToImage.route) },
                testTag = "home_image_to_image_card"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }

        // RECENT CREATIONS SECTION
        item {
            SectionHeader(
                title = "Recent Creations",
                subtitle = "Your latest AI videos and images",
                actionText = if (recentCreations.isNotEmpty()) "View All" else null,
                onActionClick = { onNavigate(Screen.Creations.route) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (recentCreations.isEmpty()) {
                EmptyState(
                    title = "Your creative canvas is empty.",
                    subtitle = "Create your first masterpiece with CineForge AI.",
                    buttonText = "Create Video",
                    onButtonClick = { onNavigate(Screen.TextToVideo.route) }
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(recentCreations) { creation ->
                        MediaCard(
                            creation = creation,
                            onClick = { selectedCreationForDetail = creation },
                            onFavoriteToggle = {
                                viewModel.toggleFavorite(creation.id, creation.isFavorite)
                            },
                            modifier = Modifier.width(220.dp)
                        )
                    }
                }
            }
        }
    }

    // Detail Dialog
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
            onRegenerate = { prompt ->
                viewModel.setPrefilledPrompt(prompt)
                onNavigate(if (creation.type.contains("VIDEO")) Screen.TextToVideo.route else Screen.TextToImage.route)
            }
        )
    }
}

@Composable
private fun QuickChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun QuickToolGridItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0F172A).copy(alpha = 0.65f),
        border = BorderStroke(1.dp, Color(0x1AFFFFFF)),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextTertiaryDark,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}
