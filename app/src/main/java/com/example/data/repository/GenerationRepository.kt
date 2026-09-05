package com.example.data.repository

import com.example.data.local.CreationDao
import com.example.data.model.CreationEntity
import com.example.data.model.CreationType
import com.example.data.remote.ApiResult
import com.example.data.remote.NetworkClient
import com.example.data.remote.PixelSterImageService
import com.example.data.remote.PixelSterTransformService
import com.example.data.remote.PixelSterVideoService
import com.example.utils.ImageUtils
import com.example.utils.PromptEnhancer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class GenerationStage(val title: String, val subtitle: String, val progress: Float) {
    PREPARING_PROMPT("Preparing prompt", "Analyzing narrative choreography and cinematic parameters...", 0.15f),
    GENERATING_FRAMES("Generating frames", "Synthesizing ultra-high definition initial visual anchor...", 0.40f),
    CREATING_MOTION("Creating motion", "Calculating optical flow and camera trajectory...", 0.65f),
    RENDERING_VIDEO("Rendering video", "Simulating physics and rendering temporal video frames...", 0.85f),
    FINALIZING("Finalizing", "Encoding stream and saving masterpiece to library...", 0.98f)
}

class GenerationRepository(
    private val imageService: PixelSterImageService = PixelSterImageService(),
    private val videoService: PixelSterVideoService = PixelSterVideoService(),
    private val transformService: PixelSterTransformService = PixelSterTransformService(),
    private val creationDao: CreationDao
) {

    suspend fun generateTextToVideo(
        rawPrompt: String,
        ratio: String = "9:16",
        duration: Int = 5,
        style: String? = "Cinematic",
        cameraMotion: String? = "Slow Push In",
        quality: String? = "High",
        negativePrompt: String? = null,
        onStageChanged: (GenerationStage) -> Unit = {}
    ): ApiResult<CreationEntity> = withContext(Dispatchers.IO) {
        try {
            // Stage 1: Preparing prompt
            onStageChanged(GenerationStage.PREPARING_PROMPT)
            val fullPrompt = PromptEnhancer.buildVideoPrompt(
                userPrompt = rawPrompt,
                style = style,
                cameraMotion = cameraMotion,
                quality = quality,
                negativePrompt = negativePrompt
            )

            // Stage 2: Generating initial frame via TTI
            onStageChanged(GenerationStage.GENERATING_FRAMES)
            val ttiResult = imageService.generateImage(prompt = fullPrompt, ratio = ratio)

            val initialImageUrl = when (ttiResult) {
                is ApiResult.Success -> {
                    val url = ttiResult.data.finalImageUrl
                    if (url.isNullOrBlank()) {
                        return@withContext ApiResult.Error("Could not generate initial scene keyframe. Please try again.")
                    }
                    url
                }
                is ApiResult.Error -> {
                    return@withContext ApiResult.Error(ttiResult.message, ttiResult.isNetworkError)
                }
            }

            // Stage 3: Preparing image & motion vectors
            onStageChanged(GenerationStage.CREATING_MOTION)
            val imageBase64 = ImageUtils.urlToBase64(NetworkClient.okHttpClient, initialImageUrl, maxDimension = 768)
            if (imageBase64.isNullOrBlank()) {
                return@withContext ApiResult.Error("Failed to prepare image frame for video animation.")
            }

            // Stage 4: Rendering video via PTV
            onStageChanged(GenerationStage.RENDERING_VIDEO)
            val ptvResult = videoService.generateVideo(
                prompt = fullPrompt,
                ratio = ratio,
                duration = duration,
                imageBase64 = imageBase64
            )

            // Stage 5: Finalizing
            onStageChanged(GenerationStage.FINALIZING)
            when (ptvResult) {
                is ApiResult.Success -> {
                    val videoUrl = ptvResult.data.finalVideoUrl
                    if (videoUrl.isNullOrBlank()) {
                        // Fallback: If videoUrl is not returned, check imageUrl or return error
                        return@withContext ApiResult.Error("Video generation completed but no video stream was returned.")
                    }

                    val entity = CreationEntity(
                        type = CreationType.TEXT_TO_VIDEO.name,
                        prompt = rawPrompt,
                        enhancedPrompt = fullPrompt,
                        mediaUrl = videoUrl,
                        previewUrl = initialImageUrl,
                        ratio = ratio,
                        duration = duration,
                        style = style,
                        cameraMotion = cameraMotion,
                        isFavorite = false,
                        timestamp = System.currentTimeMillis()
                    )
                    val id = creationDao.insertCreation(entity)
                    ApiResult.Success(entity.copy(id = id))
                }
                is ApiResult.Error -> {
                    ApiResult.Error(ptvResult.message, ptvResult.isNetworkError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error("Something went wrong while creating your video.")
        }
    }

    suspend fun animateImageToVideo(
        prompt: String,
        ratio: String = "9:16",
        duration: Int = 5,
        motionIntensity: String = "Balanced",
        imageBase64: String,
        previewSourceUri: String? = null
    ): ApiResult<CreationEntity> = withContext(Dispatchers.IO) {
        val motionPrompt = if (prompt.isNotBlank()) {
            "$prompt, motion intensity: $motionIntensity, fluid physics, cinematic stability"
        } else {
            "Cinematic organic motion, fluid animation, $motionIntensity dynamics"
        }

        val result = videoService.generateVideo(
            prompt = motionPrompt,
            ratio = ratio,
            duration = duration,
            imageBase64 = imageBase64
        )

        when (result) {
            is ApiResult.Success -> {
                val videoUrl = result.data.finalVideoUrl
                if (videoUrl.isNullOrBlank()) {
                    ApiResult.Error("Animation completed but no video link was returned.")
                } else {
                    val entity = CreationEntity(
                        type = CreationType.IMAGE_TO_VIDEO.name,
                        prompt = prompt.ifBlank { "Image Animation ($motionIntensity)" },
                        enhancedPrompt = motionPrompt,
                        mediaUrl = videoUrl,
                        previewUrl = previewSourceUri ?: result.data.imageUrl,
                        ratio = ratio,
                        duration = duration,
                        isFavorite = false,
                        timestamp = System.currentTimeMillis()
                    )
                    val id = creationDao.insertCreation(entity)
                    ApiResult.Success(entity.copy(id = id))
                }
            }
            is ApiResult.Error -> ApiResult.Error(result.message, result.isNetworkError)
        }
    }

    suspend fun generateTextToImage(
        prompt: String,
        ratio: String = "16:9",
        copies: Int = 1
    ): ApiResult<List<CreationEntity>> = withContext(Dispatchers.IO) {
        val createdList = mutableListOf<CreationEntity>()
        var lastError: String? = null

        // Support bulk generations
        val actualCopies = copies.coerceIn(1, 10)
        for (i in 0 until actualCopies) {
            val result = imageService.generateImage(prompt = prompt, ratio = ratio)
            when (result) {
                is ApiResult.Success -> {
                    val imgUrl = result.data.finalImageUrl
                    if (!imgUrl.isNullOrBlank()) {
                        val entity = CreationEntity(
                            type = CreationType.TEXT_TO_IMAGE.name,
                            prompt = prompt,
                            mediaUrl = imgUrl,
                            previewUrl = imgUrl,
                            ratio = ratio,
                            timestamp = System.currentTimeMillis()
                        )
                        val id = creationDao.insertCreation(entity)
                        createdList.add(entity.copy(id = id))
                    }
                }
                is ApiResult.Error -> {
                    lastError = result.message
                }
            }
        }

        if (createdList.isNotEmpty()) {
            ApiResult.Success(createdList)
        } else {
            ApiResult.Error(lastError ?: "Something went wrong while generating your image.")
        }
    }

    suspend fun transformImage(
        prompt: String,
        imageBase64: String,
        sourcePreviewUri: String? = null
    ): ApiResult<CreationEntity> = withContext(Dispatchers.IO) {
        val result = transformService.transformImage(
            prompt = prompt,
            imageBase64 = imageBase64
        )

        when (result) {
            is ApiResult.Success -> {
                val imageUrl = result.data.finalImageUrl
                if (imageUrl.isNullOrBlank()) {
                    ApiResult.Error("Image transformation failed to produce an image.")
                } else {
                    val entity = CreationEntity(
                        type = CreationType.IMAGE_TO_IMAGE.name,
                        prompt = prompt,
                        mediaUrl = imageUrl,
                        previewUrl = sourcePreviewUri ?: imageUrl,
                        ratio = "1:1",
                        timestamp = System.currentTimeMillis()
                    )
                    val id = creationDao.insertCreation(entity)
                    ApiResult.Success(entity.copy(id = id))
                }
            }
            is ApiResult.Error -> ApiResult.Error(result.message, result.isNetworkError)
        }
    }
}
