package com.example.data.remote

import com.example.data.model.ImageToImageRequest
import com.example.data.model.ImageToImageResponse
import com.example.data.model.ImageToVideoRequest
import com.example.data.model.ImageToVideoResponse
import com.example.data.model.TextToImageRequest
import com.example.data.model.TextToImageResponse
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val isNetworkError: Boolean = false) : ApiResult<Nothing>()
}

class PixelSterImageService(
    private val api: PixelSterApiService = NetworkClient.apiService
) {
    suspend fun generateImage(prompt: String, ratio: String): ApiResult<TextToImageResponse> {
        return safeApiCall("Something went wrong while generating your image.") {
            api.generateTextToImage(TextToImageRequest(prompt = prompt, ratio = ratio))
        }
    }
}

class PixelSterVideoService(
    private val api: PixelSterApiService = NetworkClient.apiService
) {
    suspend fun generateVideo(
        prompt: String,
        ratio: String,
        duration: Int,
        imageBase64: String
    ): ApiResult<ImageToVideoResponse> {
        return safeApiCall("Something went wrong while creating your video.") {
            api.generateVideo(
                ImageToVideoRequest(
                    prompt = prompt,
                    ratio = ratio,
                    duration = duration,
                    imageBase64 = imageBase64
                )
            )
        }
    }
}

class PixelSterTransformService(
    private val api: PixelSterApiService = NetworkClient.apiService
) {
    suspend fun transformImage(
        prompt: String,
        imageBase64: String
    ): ApiResult<ImageToImageResponse> {
        return safeApiCall("Something went wrong while transforming your image.") {
            api.transformImage(
                ImageToImageRequest(
                    prompt = prompt,
                    imageBase64 = imageBase64
                )
            )
        }
    }
}

internal suspend fun <T> safeApiCall(
    defaultErrorMessage: String,
    call: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(defaultErrorMessage)
            }
        } else {
            val errorMsg = when (response.code()) {
                400 -> "Invalid generation parameters. Please refine your prompt."
                429 -> "Generation servers are currently at high load. Please try again shortly."
                504 -> "Video generation timed out. Please retry with a shorter duration."
                500, 502, 503 -> "Server error occurred. Please try again in a few moments."
                else -> defaultErrorMessage
            }
            ApiResult.Error(errorMsg)
        }
    } catch (e: UnknownHostException) {
        ApiResult.Error("Check your internet connection and try again.", isNetworkError = true)
    } catch (e: SocketTimeoutException) {
        ApiResult.Error("Connection timed out. Video rendering takes longer under high traffic.", isNetworkError = true)
    } catch (e: IOException) {
        ApiResult.Error("Check your internet connection and try again.", isNetworkError = true)
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: defaultErrorMessage)
    }
}
