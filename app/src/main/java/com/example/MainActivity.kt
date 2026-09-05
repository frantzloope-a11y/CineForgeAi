package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.BottomNavItems
import com.example.ui.navigation.Screen
import com.example.ui.screens.about.AboutScreen
import com.example.ui.screens.about.AboutTechInfoticsScreen
import com.example.ui.screens.explore.ExploreScreen
import com.example.ui.screens.freetools.FreeToolsScreen
import com.example.ui.screens.history.CreationsScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.image.ImageToImageScreen
import com.example.ui.screens.image.TextToImageScreen
import com.example.ui.screens.library.PromptLibraryScreen
import com.example.ui.screens.promptlab.PromptLabScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.video.ImageToVideoScreen
import com.example.ui.screens.video.TextToVideoScreen
import com.example.ui.theme.CineForgeTheme
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.PrimaryGradient
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val themeMode by mainViewModel.themeMode.collectAsState()

            CineForgeTheme(themeMode = themeMode) {
                MainAppContent(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.TextToVideo.route,
        Screen.Explore.route,
        Screen.Creations.route,
        Screen.Settings.route
    )

    val shouldShowBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = Color(0xFF0A0A0A).copy(alpha = 0.96f),
                    tonalElevation = 0.dp,
                    modifier = Modifier.testTag("bottom_navigation_bar")
                ) {
                    BottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                if (item.isPrimaryAction) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(com.example.ui.theme.AmberPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = item.selectedIcon,
                                            contentDescription = item.title,
                                            tint = Color.Black,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.title.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    letterSpacing = 0.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = com.example.ui.theme.AmberPrimary,
                                selectedTextColor = com.example.ui.theme.AmberPrimary,
                                unselectedIconColor = com.example.ui.theme.TextMutedDark,
                                unselectedTextColor = com.example.ui.theme.TextMutedDark,
                                indicatorColor = Color(0x22F59E0B)
                            ),
                            modifier = Modifier.testTag("nav_item_${item.title.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(Screen.TextToVideo.route) {
                TextToVideoScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.ImageToVideo.route) {
                ImageToVideoScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.TextToImage.route) {
                TextToImageScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.ImageToImage.route) {
                ImageToImageScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Creations.route) {
                CreationsScreen(
                    viewModel = viewModel,
                    onNavigateToCreate = { navController.navigate(Screen.TextToVideo.route) },
                    onPrefillPrompt = { prompt, targetRoute ->
                        viewModel.setPrefilledPrompt(prompt)
                        navController.navigate(targetRoute)
                    }
                )
            }

            composable(Screen.PromptLab.route) {
                PromptLabScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onUseInTool = { prompt, targetRoute ->
                        viewModel.setPrefilledPrompt(prompt)
                        navController.navigate(targetRoute)
                    }
                )
            }

            composable(Screen.PromptLibrary.route) {
                PromptLibraryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onUsePrompt = { prompt ->
                        viewModel.setPrefilledPrompt(prompt)
                        navController.navigate(Screen.TextToVideo.route)
                    }
                )
            }

            composable(Screen.Explore.route) {
                ExploreScreen(
                    viewModel = viewModel,
                    onTryThis = { prompt ->
                        viewModel.setPrefilledPrompt(prompt)
                        navController.navigate(Screen.TextToVideo.route)
                    }
                )
            }

            composable(Screen.FreeTools.route) {
                FreeToolsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateInternal = { route -> navController.navigate(route) }
                )
            }

            composable(Screen.About.route) {
                AboutScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTechInfotics = { navController.navigate(Screen.AboutTechInfotics.route) }
                )
            }

            composable(Screen.AboutTechInfotics.route) {
                AboutTechInfoticsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
        }
    }
}
