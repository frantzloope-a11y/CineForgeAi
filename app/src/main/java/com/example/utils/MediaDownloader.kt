package com.example.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object MediaDownloader {

    suspend fun downloadMedia(
        context: Context,
        client: OkHttpClient,
        mediaUrl: String,
        isVideo: Boolean,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(mediaUrl).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    onError("Failed to download media: HTTP ${response.code}")
                }
                return@withContext
            }

            val body = response.body ?: run {
                withContext(Dispatchers.Main) { onError("Empty media file received") }
                return@withContext
            }

            val extension = if (isVideo) "mp4" else "jpg"
            val mimeType = if (isVideo) "video/mp4" else "image/jpeg"
            val filename = "CineForge_${System.currentTimeMillis()}.$extension"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val collection = if (isVideo) {
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                }

                val relativePath = if (isVideo) {
                    Environment.DIRECTORY_MOVIES + "/CineForge"
                } else {
                    Environment.DIRECTORY_PICTURES + "/CineForge"
                }

                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                val itemUri: Uri? = context.contentResolver.insert(collection, values)
                if (itemUri != null) {
                    context.contentResolver.openOutputStream(itemUri)?.use { output ->
                        body.byteStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                    values.clear()
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    context.contentResolver.update(itemUri, values, null, null)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Saved to ${if (isVideo) "Movies" else "Pictures"}/CineForge", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) { onError("Could not create destination file") }
                }
            } else {
                val targetDir = Environment.getExternalStoragePublicDirectory(
                    if (isVideo) Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES
                )
                val cineForgeDir = File(targetDir, "CineForge")
                if (!cineForgeDir.exists()) cineForgeDir.mkdirs()
                val targetFile = File(cineForgeDir, filename)

                FileOutputStream(targetFile).use { output ->
                    body.byteStream().use { input ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Saved to ${targetFile.absolutePath}", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onError("Download error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}
