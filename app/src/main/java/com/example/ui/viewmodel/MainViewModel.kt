package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SettingsManager
import com.example.data.local.ThemeMode
import com.example.data.model.CreationEntity
import com.example.data.remote.ApiResult
import com.example.data.repository.CreationRepository
import com.example.data.repository.GenerationRepository
import com.example.data.repository.GenerationStage
import com.example.utils.ImageUtils
import com.example.utils.PromptEnhancer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class GenerationUiState {
    object Idle : GenerationUiState()
    data class Generating(val stage: GenerationStage, val title: String) : GenerationUiState()
    data class Success(val creation: CreationEntity) : GenerationUiState()
    data class BulkSuccess(val creations: List<CreationEntity>) : GenerationUiState()
    data class Error(val message: String, val isNetworkError: Boolean = false) : GenerationUiState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val creationRepo = CreationRepository(db.creationDao())
    val generationRepo = GenerationRepository(creationDao = db.creationDao())
    val settingsManager = SettingsManager(application)

    // Theme Mode
    val themeMode: StateFlow<ThemeMode> = settingsManager.themeMode

    // All Creations Flow
    val allCreations: StateFlow<List<CreationEntity>> = creationRepo.allCreations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteCreations: StateFlow<List<CreationEntity>> = creationRepo.favoriteCreations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val videoCreations: StateFlow<List<CreationEntity>> = creationRepo.videoCreations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val imageCreations: StateFlow<List<CreationEntity>> = creationRepo.imageCreations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentCreations: StateFlow<List<CreationEntity>> = creationRepo.getRecentCreations(6)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Generation State for Text To Video
    private val _videoGenState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val videoGenState: StateFlow<GenerationUiState> = _videoGenState.asStateFlow()

    // Active Generation State for Image To Video
    private val _ptvGenState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val ptvGenState: StateFlow<GenerationUiState> = _ptvGenState.asStateFlow()

    // Active Generation State for Text To Image
    private val _imageGenState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val imageGenState: StateFlow<GenerationUiState> = _imageGenState.asStateFlow()

    // Active Generation State for Image To Image
    private val _transformGenState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val transformGenState: StateFlow<GenerationUiState> = _transformGenState.asStateFlow()

    // Transferable prompt buffer for "Use Prompt" or "Try This"
    private val _prefilledPrompt = MutableStateFlow<String?>(null)
    val prefilledPrompt: StateFlow<String?> = _prefilledPrompt.asStateFlow()

    // Video generation count for balanced ad frequency (1 interstitial per 2 video creations)
    private val _videoCreationCount = MutableStateFlow(0)
    val videoCreationCount: StateFlow<Int> = _videoCreationCount.asStateFlow()

    /**
     * Increments creation count and returns whether an interstitial ad should be triggered.
     * Policy: Shows ad after every 2 creations (count % 2 == 0).
     */
    fun shouldShowInterstitialAd(): Boolean {
        _videoCreationCount.value += 1
        return _videoCreationCount.value % 2 == 0
    }

    fun setPrefilledPrompt(prompt: String) {
        _prefilledPrompt.value = prompt
    }

    fun consumePrefilledPrompt(): String? {
        val p = _prefilledPrompt.value
        _prefilledPrompt.value = null
        return p
    }

    // Generation: Text to Video
    fun generateTextToVideo(
        prompt: String,
        ratio: String,
        duration: Int,
        style: String,
        cameraMotion: String,
        quality: String,
        negativePrompt: String?
    ) {
        viewModelScope.launch {
            _videoGenState.value = GenerationUiState.Generating(
                stage = GenerationStage.PREPARING_PROMPT,
                title = "Creating your cinematic video..."
            )

            val result = generationRepo.generateTextToVideo(
                rawPrompt = prompt,
                ratio = ratio,
                duration = duration,
                style = style,
                cameraMotion = cameraMotion,
                quality = quality,
                negativePrompt = negativePrompt,
                onStageChanged = { stage ->
                    _videoGenState.value = GenerationUiState.Generating(
                        stage = stage,
                        title = "Creating your cinematic video..."
                    )
                }
            )

            _videoGenState.value = when (result) {
                is ApiResult.Success -> GenerationUiState.Success(result.data)
                is ApiResult.Error -> GenerationUiState.Error(result.message, result.isNetworkError)
            }
        }
    }

    fun resetVideoGenState() {
        _videoGenState.value = GenerationUiState.Idle
    }

    // Generation: Image to Video
    fun animateImageToVideo(
        imageUri: Uri,
        motionPrompt: String,
        ratio: String,
        duration: Int,
        motionIntensity: String
    ) {
        viewModelScope.launch {
            _ptvGenState.value = GenerationUiState.Generating(
                stage = GenerationStage.CREATING_MOTION,
                title = "Animating your image..."
            )

            val base64 = ImageUtils.uriToBase64(getApplication(), imageUri)
            if (base64 == null) {
                _ptvGenState.value = GenerationUiState.Error("Please upload a JPG, PNG or WebP image.")
                return@launch
            }

            val result = generationRepo.animateImageToVideo(
                prompt = motionPrompt,
                ratio = ratio,
                duration = duration,
                motionIntensity = motionIntensity,
                imageBase64 = base64,
                previewSourceUri = imageUri.toString()
            )

            _ptvGenState.value = when (result) {
                is ApiResult.Success -> GenerationUiState.Success(result.data)
                is ApiResult.Error -> GenerationUiState.Error(result.message, result.isNetworkError)
            }
        }
    }

    fun resetPtvGenState() {
        _ptvGenState.value = GenerationUiState.Idle
    }

    // Generation: Text to Image
    fun generateTextToImage(prompt: String, ratio: String, copies: Int) {
        viewModelScope.launch {
            _imageGenState.value = GenerationUiState.Generating(
                stage = GenerationStage.GENERATING_FRAMES,
                title = "Synthesizing AI Artwork..."
            )

            val result = generationRepo.generateTextToImage(prompt, ratio, copies)
            _imageGenState.value = when (result) {
                is ApiResult.Success -> {
                    if (result.data.size == 1) {
                        GenerationUiState.Success(result.data.first())
                    } else {
                        GenerationUiState.BulkSuccess(result.data)
                    }
                }
                is ApiResult.Error -> GenerationUiState.Error(result.message, result.isNetworkError)
            }
        }
    }

    fun resetImageGenState() {
        _imageGenState.value = GenerationUiState.Idle
    }

    // Generation: Image to Image (Transform)
    fun transformImage(imageUri: Uri, prompt: String) {
        viewModelScope.launch {
            _transformGenState.value = GenerationUiState.Generating(
                stage = GenerationStage.GENERATING_FRAMES,
                title = "Transforming image style..."
            )

            val base64 = ImageUtils.uriToBase64(getApplication(), imageUri)
            if (base64 == null) {
                _transformGenState.value = GenerationUiState.Error("Please upload a JPG, PNG or WebP image.")
                return@launch
            }

            val result = generationRepo.transformImage(
                prompt = prompt,
                imageBase64 = base64,
                sourcePreviewUri = imageUri.toString()
            )

            _transformGenState.value = when (result) {
                is ApiResult.Success -> GenerationUiState.Success(result.data)
                is ApiResult.Error -> GenerationUiState.Error(result.message, result.isNetworkError)
            }
        }
    }

    fun resetTransformGenState() {
        _transformGenState.value = GenerationUiState.Idle
    }

    // Creations Management
    fun toggleFavorite(id: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            creationRepo.toggleFavorite(id, !isFavorite)
        }
    }

    fun deleteCreation(id: Long) {
        viewModelScope.launch {
            creationRepo.deleteById(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            creationRepo.clearHistory()
        }
    }

    // Settings
    fun setThemeMode(mode: ThemeMode) {
        settingsManager.setThemeMode(mode)
    }

    fun toggleTheme() {
        val current = themeMode.value
        val next = if (current == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
        settingsManager.setThemeMode(next)
    }

    fun clearCache() {
        settingsManager.clearCache(getApplication())
    }
}
