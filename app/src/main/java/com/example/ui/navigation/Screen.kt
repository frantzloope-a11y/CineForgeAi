package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VideoCameraBack
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object CreateHub : Screen("create_hub")
    object TextToVideo : Screen("text_to_video")
    object ImageToVideo : Screen("image_to_video")
    object TextToImage : Screen("text_to_image")
    object ImageToImage : Screen("image_to_image")
    object Creations : Screen("creations")
    object PromptLab : Screen("prompt_lab")
    object PromptLibrary : Screen("prompt_library")
    object Explore : Screen("explore")
    object FreeTools : Screen("free_tools")
    object About : Screen("about")
    object AboutTechInfotics : Screen("about_techinfotics")
    object Settings : Screen("settings")
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val isPrimaryAction: Boolean = false
)

val BottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Screen.TextToVideo.route,
        title = "Create",
        selectedIcon = Icons.Filled.VideoCameraBack,
        unselectedIcon = Icons.Outlined.VideoCameraBack,
        isPrimaryAction = true
    ),
    BottomNavItem(
        route = Screen.Explore.route,
        title = "Explore",
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore
    ),
    BottomNavItem(
        route = Screen.Creations.route,
        title = "Creations",
        selectedIcon = Icons.Filled.Collections,
        unselectedIcon = Icons.Outlined.Collections
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)
