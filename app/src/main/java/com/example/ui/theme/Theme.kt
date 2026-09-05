package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.local.ThemeMode

// Sophisticated Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = AmberPrimary,
    onPrimary = Color.Black,
    primaryContainer = OrangeDeep,
    onPrimaryContainer = Color.White,
    secondary = OrangeAccent,
    onSecondary = Color.Black,
    secondaryContainer = DarkCardElevated,
    onSecondaryContainer = AmberBright,
    tertiary = CyanAccent,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextTertiaryDark,
    outline = DarkBorder,
    outlineVariant = DarkGlassBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF), // iOS System Blue for clear crisp actions
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE5F1FF),
    onPrimaryContainer = Color(0xFF0040DD),
    secondary = Color(0xFF5856D6), // iOS Indigo
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEBEBF5),
    onSecondaryContainer = Color(0xFF1C1C1E),
    tertiary = Color(0xFF34C759), // iOS System Green
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
    outlineVariant = Color(0xFFE5E5EA).copy(alpha = 0.6f)
)

@Composable
fun CineForgeTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
