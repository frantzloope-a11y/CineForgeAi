package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Sophisticated Dark Palette
val DarkBackground = Color(0xFF0A0A0A) // Deepest Obsidian
val DarkSurface = Color(0xFF0F172A)    // Slate 900
val DarkCard = Color(0xFF141722)       // Slate 900 / 60
val DarkCardElevated = Color(0xFF1E2433)
val DarkBorder = Color(0x1AFFFFFF)     // border-white/10
val DarkGlass = Color(0x990F172A)
val DarkGlassBorder = Color(0x0FFFFFFF) // border-white/5

// Sophisticated Dark Brand & Accent Colors
val AmberPrimary = Color(0xFFF59E0B)   // amber-500
val AmberBright = Color(0xFFFBBF24)    // amber-400
val OrangeAccent = Color(0xFFF97316)   // orange-500
val OrangeDeep = Color(0xFFEA580C)     // orange-600

// Secondary Accent Colors
val CyanAccent = Color(0xFF38BDF8)     // blue-400 / cyan
val ElectricIndigo = Color(0xFF818CF8) // indigo-400
val VioletGlow = Color(0xFFA855F7)     // purple-400
val AmberGlow = Color(0xFFF59E0B)      // amber-500
val RoseGlow = Color(0xFFF43F5E)       // rose-500
val EmeraldGlow = Color(0xFF10B981)    // emerald-500

// Text Colors (Slate scale from design)
val TextPrimaryDark = Color(0xFFF1F5F9)   // slate-100
val TextSecondaryDark = Color(0xFFCBD5E1) // slate-300
val TextTertiaryDark = Color(0xFF94A3B8)  // slate-400
val TextMutedDark = Color(0xFF64748B)     // slate-500

// Light Theme Palette (Warm Editorial Contrast)
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightCard = Color(0xFFFFFFFF)
val LightCardElevated = Color(0xFFF1F5F9)
val LightBorder = Color(0xFFE2E8F0)
val LightGlass = Color(0xCCFFFFFF)
val LightGlassBorder = Color(0x1F000000)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF334155)
val TextTertiaryLight = Color(0xFF64748B)

// Gradients (Sophisticated Dark)
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(AmberPrimary, OrangeAccent, OrangeDeep)
)

val AmberOrangeGradient = Brush.horizontalGradient(
    colors = listOf(AmberBright, OrangeAccent)
)

val HeroCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF181D2A),
        Color(0xFF121520),
        Color(0xFF0A0A0A)
    )
)

val AmberGlowRadial = Brush.radialGradient(
    colors = listOf(Color(0x3DF59E0B), Color(0x00000000))
)

val HeroBorderGradient = Brush.horizontalGradient(
    colors = listOf(Color(0x4DF59E0B), Color(0x33EA580C), Color(0x1AFFFFFF))
)
