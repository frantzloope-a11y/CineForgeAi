package com.example.data.remote

import com.example.data.model.ImageToImageRequest
import com.example.data.model.ImageToImageResponse
import com.example.data.model.ImageToVideoRequest
import com.example.data.model.ImageToVideoResponse
import com.example.data.model.TextToImageRequest
import com.example.data.model.TextToImageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PixelSterApiService {

    @POST("/api/tti")
    suspend fun generateTextToImage(
        @Body request: TextToImageRequest
    ): Response<TextToImageResponse>

    @POST("/api/pti")
    suspend fun transformImage(
        @Body request: ImageToImageRequest
    ): Response<ImageToImageResponse>

    @POST("/api/ptv")
    suspend fun generateVideo(
        @Body request: ImageToVideoRequest
    ): Response<ImageToVideoResponse>
}
