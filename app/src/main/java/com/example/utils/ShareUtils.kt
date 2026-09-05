package com.example.utils

import android.content.Context
import android.content.Intent

object ShareUtils {

    fun shareText(context: Context, text: String, title: String = "Share Prompt") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, title))
    }

    fun shareMedia(context: Context, mediaUrl: String, prompt: String, isVideo: Boolean) {
        val shareMessage = buildString {
            append(if (isVideo) "🎬 Check out this AI video created with CineForge AI!\n\n" else "✨ Check out this AI artwork created with CineForge AI!\n\n")
            append("\"$prompt\"\n\n")
            append("Media Link: $mediaUrl\n\n")
            append("Created with CineForge AI — Turn Ideas Into Motion.")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            putExtra(Intent.EXTRA_SUBJECT, if (isVideo) "CineForge AI Video" else "CineForge AI Image")
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }
}
