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
    primary = OrangeDeep,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7), // Amber 100
    onPrimaryContainer = Color(0xFF78350F), // Amber 900
    secondary = AmberPrimary,
    onSecondary = Color.Black,
    secondaryContainer = LightCardElevated,
    onSecondaryContainer = Color(0xFF0F172A),
    tertiary = CyanAccent,
    onTertiary = Color.Black,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCardElevated,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
    outlineVariant = LightGlassBorder
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
