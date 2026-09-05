package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

enum class CreationType {
    TEXT_TO_VIDEO,
    IMAGE_TO_VIDEO,
    TEXT_TO_IMAGE,
    IMAGE_TO_IMAGE
}

@Entity(tableName = "creations")
data class CreationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // TEXT_TO_VIDEO, IMAGE_TO_VIDEO, TEXT_TO_IMAGE, IMAGE_TO_IMAGE
    val prompt: String,
    val enhancedPrompt: String? = null,
    val mediaUrl: String,
    val previewUrl: String? = null,
    val ratio: String = "9:16",
    val duration: Int = 5,
    val style: String? = null,
    val cameraMotion: String? = null,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

// Network Request & Response Models

@JsonClass(generateAdapter = true)
data class TextToImageRequest(
    @Json(name = "prompt") val prompt: String,
    @Json(name = "ratio") val ratio: String = "16:9"
)

@JsonClass(generateAdapter = true)
data class TextToImageResponse(
    @Json(name = "success") val success: Boolean? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "code") val code: String? = null,
    @Json(name = "prompt") val prompt: String? = null,
    @Json(name = "ratio") val ratio: String? = null,
    @Json(name = "model") val model: String? = null,
    @Json(name = "error") val error: String? = null,
    @Json(name = "message") val message: String? = null
) {
    val finalImageUrl: String?
        get() = imageUrl ?: url
}

@JsonClass(generateAdapter = true)
data class ImageToImageRequest(
    @Json(name = "prompt") val prompt: String,
    @Json(name = "imageBase64") val imageBase64: String
)

@JsonClass(generateAdapter = true)
data class ImageToImageResponse(
    @Json(name = "success") val success: Boolean? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "error") val error: String? = null,
    @Json(name = "message") val message: String? = null
) {
    val finalImageUrl: String?
        get() = imageUrl ?: url
}

@JsonClass(generateAdapter = true)
data class ImageToVideoRequest(
    @Json(name = "prompt") val prompt: String,
    @Json(name = "ratio") val ratio: String = "9:16",
    @Json(name = "duration") val duration: Int = 5,
    @Json(name = "imageBase64") val imageBase64: String
)

@JsonClass(generateAdapter = true)
data class ImageToVideoResponse(
    @Json(name = "success") val success: Boolean? = null,
    @Json(name = "videoUrl") val videoUrl: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "prompt") val prompt: String? = null,
    @Json(name = "ratio") val ratio: String? = null,
    @Json(name = "error") val error: String? = null,
    @Json(name = "message") val message: String? = null
) {
    val finalVideoUrl: String?
        get() = videoUrl ?: url
}
