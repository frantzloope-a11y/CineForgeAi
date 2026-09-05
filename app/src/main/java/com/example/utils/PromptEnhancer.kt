package com.example.utils

enum class EnhancementMode(val label: String, val description: String) {
    ENHANCE("Smart Enhance", "Intelligently balances lighting, motion, and visual clarity"),
    CINEMATIC("Cinematic 35mm", "Panavision anamorphic lens, shallow depth of field, 24fps motion blur"),
    PHOTOREALISTIC("Photorealistic 8K", "Octane render, photorealistic textures, HDR volumetric illumination"),
    COMMERCIAL("Luxury Commercial", "Clean studio rim light, high key reflections, sleek product aesthetic"),
    SOCIAL_MEDIA("Viral Reel / TikTok", "Dynamic vertical framing, vibrant color grading, high engagement hook"),
    ANIME("Studio Anime", "Makoto Shinkai style, vivid pastel sky, detailed cel shading"),
    REWRITE("Creative Rewrite", "Reimagines the concept with rich poetic narrative details")
}

object PromptEnhancer {

    fun enhance(basePrompt: String, mode: EnhancementMode = EnhancementMode.ENHANCE): String {
        val clean = basePrompt.trim().removeSuffix(".")
        if (clean.isBlank()) return "A breathtaking cinematic masterpiece with atmospheric volumetric light and rich details"

        return when (mode) {
            EnhancementMode.ENHANCE -> {
                "$clean, highly detailed, masterfully shot, atmospheric lighting, dynamic camera motion, rich cinematic textures, 8k resolution, photorealistic masterpiece"
            }
            EnhancementMode.CINEMATIC -> {
                "Cinematic feature film shot: $clean, anamorphic 35mm lens, shallow depth of field, subtle film grain, dramatic chiaroscuro lighting, color graded in DaVinci Resolve, 24fps motion blur"
            }
            EnhancementMode.PHOTOREALISTIC -> {
                "Ultra-photorealistic shot of $clean, ray-traced reflections, subsurface scattering, 8k raw photo, sharp focus, natural sunlight, hyper-detailed textures, Octane render"
            }
            EnhancementMode.COMMERCIAL -> {
                "High-end luxury commercial advertisement: $clean, pristine studio lighting, soft rim light highlights, elegant minimalist composition, ultra-crisp premium commercial look"
            }
            EnhancementMode.SOCIAL_MEDIA -> {
                "Viral aesthetic 4k short video of $clean, dynamic sweeping movement, punchy vibrant colors, high visual clarity, trendy TikTok and Instagram Reels cinematography"
            }
            EnhancementMode.ANIME -> {
                "Masterpiece anime movie visual: $clean, Makoto Shinkai and Studio Ghibli inspired, vibrant atmospheric clouds, ethereal glowing lighting, hand-drawn aesthetic, stunning color depth"
            }
            EnhancementMode.REWRITE -> {
                "An awe-inspiring, emotionally resonant scene portraying $clean, bathed in golden hour luminescence, evocative environmental storytelling, epic sense of scale, poetic motion"
            }
        }
    }

    fun buildVideoPrompt(
        userPrompt: String,
        style: String?,
        cameraMotion: String?,
        quality: String?,
        negativePrompt: String?
    ): String {
        val sb = StringBuilder(userPrompt.trim())

        if (!cameraMotion.isNullOrBlank() && cameraMotion != "Static") {
            sb.append(", with $cameraMotion camera motion")
        }

        if (!style.isNullOrBlank()) {
            when (style) {
                "Cinematic" -> sb.append(", 35mm film look, cinematic color grade, 24fps motion blur")
                "Realistic" -> sb.append(", ultra-realistic documentary footage, authentic natural lighting")
                "Anime" -> sb.append(", Japanese anime aesthetic, vibrant colors, Makoto Shinkai inspired")
                "3D" -> sb.append(", 3D CGI animation, Pixar and Unreal Engine 5 render style")
                "Fantasy" -> sb.append(", mythical fantasy atmosphere, magical glowing particle effects")
                "Product Commercial" -> sb.append(", luxury product commercial aesthetic, high-gloss rim lighting")
                "Documentary" -> sb.append(", BBC Earth 4k nature documentary cinematography, realistic physics")
                "Vlog" -> sb.append(", natural handheld POV vlog perspective, authentic ambient colors")
                "Cyberpunk" -> sb.append(", neon cyberpunk reflections, rainy streets, holographic cyan and magenta accents")
                "Minimal" -> sb.append(", clean minimalist composition, high contrast, elegant negative space")
            }
        }

        if (!quality.isNullOrBlank()) {
            when (quality) {
                "High" -> sb.append(", high definition, smooth fluid animation")
                "Ultra" -> sb.append(", ultra-high quality, 8k crisp details, lifelike motion simulation")
                else -> {}
            }
        }

        if (!negativePrompt.isNullOrBlank()) {
            sb.append(" [negative: ${negativePrompt.trim()}]")
        }

        return sb.toString()
    }
}
