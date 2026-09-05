package com.example.ui.screens.settings

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import android.app.Activity
import com.example.ads.UnityAdsManager
import com.example.data.local.ThemeMode
import com.example.ui.components.SectionHeader
import com.example.ui.components.SettingsRow
import com.example.ui.components.SettingsSwitchRow
import com.example.ui.navigation.Screen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val themeMode by viewModel.themeMode.collectAsState()
    val defaultRatio by viewModel.settingsManager.defaultRatio.collectAsState()
    val defaultDuration by viewModel.settingsManager.defaultDuration.collectAsState()
    val defaultStyle by viewModel.settingsManager.defaultStyle.collectAsState()
    val autoEnhance by viewModel.settingsManager.autoEnhance.collectAsState()

    var showClearHistoryDialog by remember { mutableStateOf(false) }

    val themeModes = listOf(
        ThemeMode.DARK to "Dark",
        ThemeMode.LIGHT to "Light",
        ThemeMode.SYSTEM to "System"
    )

    val ratioOptions = listOf("9:16", "16:9", "1:1")
    val durationOptions = listOf(5, 10, 15, 20)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(top = 20.dp, bottom = 90.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Preferences, generation defaults and app identity.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // APPEARANCE SECTION
        item {
            SectionHeader(title = "Appearance", subtitle = "Choose your preferred color theme")

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    themeModes.forEach { (mode, label) ->
                        val isSelected = themeMode == mode
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) ElectricIndigo.copy(alpha = 0.35f) else Color.Transparent,
                            border = if (isSelected) BorderStroke(1.dp, CyanAccent) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setThemeMode(mode) }
                                .testTag("theme_mode_$label")
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // GENERATION DEFAULTS
        item {
            SectionHeader(title = "Generation Defaults", subtitle = "Initial values for the video composer")

            Spacer(modifier = Modifier.height(6.dp))

            // Default Ratio
            SettingsRow(
                title = "Default Aspect Ratio",
                subtitle = "Preferred orientation for video canvas",
                icon = Icons.Default.Tune,
                trailingContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ratioOptions.forEach { ratio ->
                            val isSel = defaultRatio == ratio
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) CyanAccent else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.settingsManager.setDefaultRatio(ratio) }
                            ) {
                                Text(
                                    text = ratio,
                                    color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Default Duration
            SettingsRow(
                title = "Default Duration",
                subtitle = "Video length in seconds",
                icon = Icons.Default.Timer,
                trailingContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        durationOptions.forEach { dur ->
                            val isSel = defaultDuration == dur
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) CyanAccent else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.settingsManager.setDefaultDuration(dur) }
                            ) {
                                Text(
                                    text = "${dur}s",
                                    color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Auto Enhance Switch
            SettingsSwitchRow(
                title = "Auto-Enhance Prompts",
                subtitle = "Automatically optimize prompts with cinematic camera rules",
                checked = autoEnhance,
                onCheckedChange = { viewModel.settingsManager.setAutoEnhance(it) },
                icon = Icons.Default.AutoAwesome
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // STORAGE & CACHE
        item {
            SectionHeader(title = "Storage & Cache", subtitle = "Manage local files and memory")

            Spacer(modifier = Modifier.height(6.dp))

            SettingsRow(
                title = "Clear Cached Media",
                subtitle = "Free temporary files and cached render thumbnails",
                icon = Icons.Default.CleaningServices,
                onClick = {
                    viewModel.clearCache()
                    Toast.makeText(context, "Temporary cache cleared", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            SettingsRow(
                title = "Clear All Creations",
                subtitle = "Permanently remove your local generation history",
                icon = Icons.Default.DeleteForever,
                onClick = { showClearHistoryDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // ABOUT LINKS & INFO
        item {
            SectionHeader(title = "About & Information", subtitle = "Platform resources and engine details")

            Spacer(modifier = Modifier.height(6.dp))

            SettingsRow(
                title = "About CineForge AI",
                subtitle = "Philosophy, creative workflow, and architecture",
                icon = Icons.Default.Info,
                onClick = { onNavigate(Screen.About.route) },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            SettingsRow(
                title = "About TechInfotics",
                subtitle = "AI technology learning and creative platform",
                icon = Icons.Default.Public,
                onClick = { onNavigate(Screen.AboutTechInfotics.route) },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Engine Info Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Backend Engine", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PixelSter Neural API", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Engine Status", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("● Online & Operational", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("App Version", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("1.0.0", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Unity Monetization Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0F172A).copy(alpha = 0.8f),
                border = BorderStroke(1.dp, com.example.ui.theme.AmberPrimary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val isInitialized by UnityAdsManager.isInitialized.collectAsState()
                    val lastStatus by UnityAdsManager.lastStatus.collectAsState()
                    val isInterstitialLoaded by UnityAdsManager.isInterstitialLoaded.collectAsState()
                    val isRewardedLoaded by UnityAdsManager.isRewardedLoaded.collectAsState()
                    var isTestMode by remember { mutableStateOf(UnityAdsManager.isTestMode) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Unity Monetization",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.AmberBright
                        )
                        Text(
                            text = if (isInitialized) "● Initialized" else "○ Offline",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isInitialized) Color(0xFF10B981) else Color(0xFFEF4444)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Game ID: ${UnityAdsManager.GAME_ID}\nBanners, Interstitials & Rewarded Video",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Status: $lastStatus\nReady: Interstitial ${if (isInterstitialLoaded) "✓" else "⏳"}, Rewarded ${if (isRewardedLoaded) "✓" else "⏳"}",
                        fontSize = 11.sp,
                        color = com.example.ui.theme.AmberBright,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Test mode toggle for developer testing
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Test Ads Mode (Simulate Ads)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = isTestMode,
                            onCheckedChange = { newMode ->
                                isTestMode = newMode
                                UnityAdsManager.initialize(context, testMode = newMode)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = com.example.ui.theme.AmberPrimary,
                                checkedTrackColor = com.example.ui.theme.AmberPrimary.copy(alpha = 0.5f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val act = context as? Activity
                                if (act != null) {
                                    UnityAdsManager.showInterstitial(act)
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Test Interstitial", fontSize = 11.sp, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = {
                                val act = context as? Activity
                                if (act != null) {
                                    UnityAdsManager.showRewarded(
                                        activity = act,
                                        onRewardEarned = {
                                            Toast.makeText(context, "✦ Rewarded Ad completed! Reward granted.", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, com.example.ui.theme.AmberPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Test Rewarded", fontSize = 11.sp, color = com.example.ui.theme.AmberBright)
                        }
                    }
                }
            }
        }
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text("Clear All Creations?") },
            text = { Text("Are you sure you want to permanently delete all saved videos and images from your local library?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showClearHistoryDialog = false
                        Toast.makeText(context, "History deleted", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Delete All", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
