package com.example.ui.screens.about

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.Screen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.PrimaryGradient

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTechInfotics: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp)
            .testTag("about_screen"),
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
                        text = "About CineForge AI",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Turn Ideas Into Motion",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyanAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // What CineForge AI Does
        item {
            AboutCard(
                title = "What is CineForge AI?",
                content = "CineForge AI is a state-of-the-art mobile creative studio designed for filmmakers, digital creators, visual artists, and AI enthusiasts. It transforms text prompts into cinematic video clips, synthesizes ultra-high resolution artworks, animates photography with realistic physics, and applies stylistic transformations with zero setup or subscription friction."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Creative Workflow
        item {
            AboutCard(
                title = "Creative Workflow",
                content = "1. Ideate: Compose your concept or use PromptLab to enrich it.\n2. Choreograph: Select aspect ratio, camera motion vectors, duration, and stylistic mood.\n3. Synthesize: Our neural pipeline generates anchor keyframes and computes temporal motion dynamics.\n4. Export & Share: Download high-definition MP4 videos directly to your device gallery or share instantly."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Free Access Philosophy
        item {
            AboutCard(
                title = "Free Access Philosophy",
                content = "CineForge AI believes that generative creativity should be universally accessible. All tools—including Text to Video, Text to Image, Image to Video, and Image Transformation—are available without mandatory paywalls, credit countdowns, or intrusive registration."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Privacy & Terms
        item {
            AboutCard(
                title = "Privacy & Local Storage",
                content = "Your generation prompts and saved media history reside locally on your device via an encrypted SQLite Room database. Media downloads are saved into standard Android MediaStore directories (Movies & Pictures). We do not collect, monetize, or harvest personal analytics."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Engine Credits & PixelSter
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Engine Architecture & Credits",
                        style = MaterialTheme.typography.titleMedium,
                        color = CyanAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Generative video rendering and image synthesis endpoints are powered by PixelSter and high-performance neural clusters.\n\nVersion: 1.0.0 (Production Release)\nEngine Status: Online & Optimized",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Link to TechInfotics
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = ElectricIndigo.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = onNavigateToTechInfotics)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Learn More at TechInfotics",
                            style = MaterialTheme.typography.titleMedium,
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Explore AI tutorials, prompt engineering guides, and creative workflows.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Open",
                        tint = CyanAccent
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutCard(title: String, content: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 21.sp
            )
        }
    }
}
