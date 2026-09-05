package com.example.data.model

data class FreeToolItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val tag: String = "Free",
    val actionType: ToolActionType = ToolActionType.INTERNAL_NAV,
    val targetRouteOrUrl: String
)

enum class ToolActionType {
    INTERNAL_NAV,
    EXTERNAL_BROWSER
}

object FreeToolsData {
    val categories = listOf(
        "AI Video", "AI Image", "AI Productivity", "AI Creator Tools", "Developer Tools"
    )

    val tools: List<FreeToolItem> = listOf(
        // AI Video
        FreeToolItem(
            id = "t_vid_1",
            name = "Text to Video Studio",
            category = "AI Video",
            description = "Generate cinematic 1080p AI video clips from natural language scene descriptions.",
            tag = "100% Free",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "text_to_video"
        ),
        FreeToolItem(
            id = "t_vid_2",
            name = "Image Animator (PTV)",
            category = "AI Video",
            description = "Bring static photography to life with natural physics, camera tracking, and fluid motion.",
            tag = "100% Free",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "image_to_video"
        ),

        // AI Image
        FreeToolItem(
            id = "t_img_1",
            name = "Text to Image Engine",
            category = "AI Image",
            description = "Synthesize ultra-detailed photorealistic artwork, renders, and designs in 11 aspect ratios.",
            tag = "100% Free",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "text_to_image"
        ),
        FreeToolItem(
            id = "t_img_2",
            name = "Image Style Transformer",
            category = "AI Image",
            description = "Transform source photos into anime, cyberpunk, 3D render, or commercial styles.",
            tag = "100% Free",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "image_to_image"
        ),

        // AI Productivity
        FreeToolItem(
            id = "t_prod_1",
            name = "PromptLab AI Enhancer",
            category = "AI Productivity",
            description = "Transform simple one-line ideas into rich cinematic prompts with lighting and camera parameters.",
            tag = "Included",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "prompt_lab"
        ),
        FreeToolItem(
            id = "t_prod_2",
            name = "Curated Prompt Library",
            category = "AI Productivity",
            description = "Browse over 30 tested prompt templates across 13 cinematic, ad, and artistic genres.",
            tag = "Included",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "prompt_library"
        ),

        // AI Creator Tools
        FreeToolItem(
            id = "t_crt_1",
            name = "Aspect Ratio Optimizer",
            category = "AI Creator Tools",
            description = "Pre-configured format ratios tailored for TikTok (9:16), YouTube (16:9), and Instagram (1:1).",
            tag = "Free",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "text_to_video"
        ),
        FreeToolItem(
            id = "t_crt_2",
            name = "Cinematic Camera Controls",
            category = "AI Creator Tools",
            description = "10 camera choreography presets including Drone Shot, Dolly Zoom, Tracking, and Orbit.",
            tag = "Free",
            actionType = ToolActionType.INTERNAL_NAV,
            targetRouteOrUrl = "text_to_video"
        ),

        // Developer Tools
        FreeToolItem(
            id = "t_dev_1",
            name = "PixelSter Generation API",
            category = "Developer Tools",
            description = "High-throughput free API endpoints for programmatic text-to-image and picture-to-video.",
            tag = "Open API",
            actionType = ToolActionType.EXTERNAL_BROWSER,
            targetRouteOrUrl = "https://ahm7xmakki.com"
        ),
        FreeToolItem(
            id = "t_dev_2",
            name = "TechInfotics Ecosystem",
            category = "Developer Tools",
            description = "Explore cutting-edge tech articles, AI tools, tutorials, and developer resources.",
            tag = "Learning",
            actionType = ToolActionType.EXTERNAL_BROWSER,
            targetRouteOrUrl = "https://techinfotics.online/"
        )
    )
}
