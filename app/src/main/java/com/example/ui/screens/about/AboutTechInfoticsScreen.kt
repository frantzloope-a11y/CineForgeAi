package com.example.ui.screens.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.School
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
import com.example.ui.components.GradientButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.PrimaryGradient

@Composable
fun AboutTechInfoticsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val websiteUrl = "https://techinfotics.online/"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp)
            .testTag("about_techinfotics_screen"),
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
                        text = "About TechInfotics",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AI & Technology Learning Platform",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyanAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Platform Overview
        item {
            TechCard(
                title = "Platform Overview",
                content = "TechInfotics is a premier educational and technological resource destination dedicated to modern Artificial Intelligence, machine learning breakthroughs, software development, and generative media production. It provides creators and engineers with the insights necessary to master emerging AI paradigms."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Creative AI Tools
        item {
            TechCard(
                title = "Creative AI Tools",
                content = "From cutting-edge text-to-video architectures and neural image enhancement pipelines to automated content workflows, TechInfotics demystifies complex generative models into actionable tools that empower creators worldwide."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Learning Resources
        item {
            TechCard(
                title = "Learning Resources",
                content = "Access deep-dive technical tutorials, prompt engineering masterclasses, API integration blueprints, and comprehensive breakdowns of computer vision and diffusion technologies."
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Mission
        item {
            TechCard(
                title = "Our Mission",
                content = "To bridge the gap between frontier artificial intelligence research and practical daily creative execution, ensuring that next-generation generative technology remains empowering, understandable, and widely accessible."
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // CTA: Visit TechInfotics
        item {
            GradientButton(
                text = "Visit TechInfotics →",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
                    context.startActivity(intent)
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                testTag = "visit_techinfotics_button"
            )
        }
    }
}

@Composable
private fun TechCard(title: String, content: String) {
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
