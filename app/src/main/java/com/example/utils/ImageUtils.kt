package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.max

object ImageUtils {

    suspend fun uriToBase64(context: Context, uri: Uri, maxDimension: Int = 1024): String? =
        withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return@withContext null
                inputStream?.close()

                val resized = resizeBitmap(originalBitmap, maxDimension)
                val outputStream = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                val bytes = outputStream.toByteArray()
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    suspend fun urlToBase64(client: OkHttpClient, imageUrl: String, maxDimension: Int = 1024): String? =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(imageUrl).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@withContext null
                val bytes = response.body?.bytes() ?: return@withContext null
                val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@withContext null
                val resized = resizeBitmap(originalBitmap, maxDimension)
                val outputStream = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    private fun resizeBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val maxSide = max(width, height)
        if (maxSide <= maxDimension) return bitmap

        val ratio = maxDimension.toFloat() / maxSide
        val targetWidth = (width * ratio).toInt()
        val targetHeight = (height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }
}
