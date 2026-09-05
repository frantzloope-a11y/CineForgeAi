package com.example.ui.screens.history

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreationEntity
import com.example.ui.components.EmptyState
import com.example.ui.components.MediaCard
import com.example.ui.components.MediaDetailDialog
import com.example.ui.navigation.Screen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CreationsScreen(
    viewModel: MainViewModel,
    onNavigateToCreate: () -> Unit,
    onPrefillPrompt: (String, String) -> Unit
) {
    val allList by viewModel.allCreations.collectAsState()
    val videosList by viewModel.videoCreations.collectAsState()
    val imagesList by viewModel.imageCreations.collectAsState()
    val favoritesList by viewModel.favoriteCreations.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedCreationForDetail by remember { mutableStateOf<CreationEntity?>(null) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    val tabs = listOf("All", "Videos", "Images", "Favorites")

    val currentItems = when (selectedTabIndex) {
        0 -> allList
        1 -> videosList
        2 -> imagesList
        3 -> favoritesList
        else -> allList
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp)
            .testTag("creations_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Creations",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${allList.size} items stored locally",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (allList.isNotEmpty()) {
                IconButton(
                    onClick = { showClearConfirmDialog = true },
                    modifier = Modifier.testTag("clear_history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear All",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Filter Tabs
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                tabs.forEachIndexed { index, tabName ->
                    val isSelected = selectedTabIndex == index
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) ElectricIndigo.copy(alpha = 0.35f) else Color.Transparent,
                        border = if (isSelected) BorderStroke(1.dp, CyanAccent) else null,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedTabIndex = index }
                            .testTag("creations_tab_$tabName")
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabName,
                                color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content List
        if (currentItems.isEmpty()) {
            EmptyState(
                title = "No creations yet.",
                subtitle = "Your next masterpiece starts with a prompt.",
                buttonText = "Create Video",
                onButtonClick = onNavigateToCreate,
                icon = Icons.Default.Collections,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(currentItems, key = { it.id }) { item ->
                    MediaCard(
                        creation = item,
                        onClick = { selectedCreationForDetail = item },
                        onFavoriteToggle = {
                            viewModel.toggleFavorite(item.id, item.isFavorite)
                        }
                    )
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
                val targetRoute = if (creation.type.contains("VIDEO")) {
                    Screen.TextToVideo.route
                } else {
                    Screen.TextToImage.route
                }
                onPrefillPrompt(prompt, targetRoute)
            }
        )
    }

    // Clear History Confirmation Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Clear All Creations?") },
            text = { Text("This will permanently remove all stored videos and images from your local library.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showClearConfirmDialog = false
                    }
                ) {
                    Text("Clear All", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
