package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    DARK,
    LIGHT,
    SYSTEM
}

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("cineforge_settings", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _defaultRatio = MutableStateFlow(prefs.getString("default_ratio", "9:16") ?: "9:16")
    val defaultRatio: StateFlow<String> = _defaultRatio.asStateFlow()

    private val _defaultDuration = MutableStateFlow(prefs.getInt("default_duration", 5))
    val defaultDuration: StateFlow<Int> = _defaultDuration.asStateFlow()

    private val _defaultStyle = MutableStateFlow(prefs.getString("default_style", "Cinematic") ?: "Cinematic")
    val defaultStyle: StateFlow<String> = _defaultStyle.asStateFlow()

    private val _autoEnhance = MutableStateFlow(prefs.getBoolean("auto_enhance", true))
    val autoEnhance: StateFlow<Boolean> = _autoEnhance.asStateFlow()

    private fun loadThemeMode(): ThemeMode {
        val saved = prefs.getString("theme_mode", ThemeMode.DARK.name)
        return try {
            ThemeMode.valueOf(saved ?: ThemeMode.DARK.name)
        } catch (e: Exception) {
            ThemeMode.DARK
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _themeMode.value = mode
    }

    fun setDefaultRatio(ratio: String) {
        prefs.edit().putString("default_ratio", ratio).apply()
        _defaultRatio.value = ratio
    }

    fun setDefaultDuration(duration: Int) {
        prefs.edit().putInt("default_duration", duration).apply()
        _defaultDuration.value = duration
    }

    fun setDefaultStyle(style: String) {
        prefs.edit().putString("default_style", style).apply()
        _defaultStyle.value = style
    }

    fun setAutoEnhance(enabled: Boolean) {
        prefs.edit().putBoolean("auto_enhance", enabled).apply()
        _autoEnhance.value = enabled
    }

    fun clearCache(context: Context) {
        try {
            context.cacheDir.deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
