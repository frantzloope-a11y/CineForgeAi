package com.example.data.model

data class PromptItem(
    val id: String,
    val category: String,
    val title: String,
    val prompt: String,
    val recommendedRatio: String = "9:16",
    val style: String = "Cinematic",
    val cameraMotion: String = "Drone Shot"
)

object PromptLibraryData {
    val categories = listOf(
        "Cinematic", "Nature", "Travel", "Product Ads", "Fashion",
        "Food", "Architecture", "Characters", "Anime", "Fantasy",
        "Social Media", "YouTube", "Short Films"
    )

    val prompts: List<PromptItem> = listOf(
        // Cinematic
        PromptItem(
            id = "cin_1",
            category = "Cinematic",
            title = "Midnight Cyber Alley",
            prompt = "A cinematic slow pan through a rainy neon-lit neo-Tokyo alley, holographic reflections in puddles, dramatic shadows, anamorphic lens flare, 8k resolution, photorealistic",
            recommendedRatio = "16:9",
            style = "Cyberpunk",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "cin_2",
            category = "Cinematic",
            title = "Golden Hour Desert Caravan",
            prompt = "Epic aerial tracking shot of a nomad traveler and camel crossing vast golden sand dunes during a radiant sunset, atmospheric dust haze, cinematic color grading, 35mm film grain",
            recommendedRatio = "16:9",
            style = "Cinematic",
            cameraMotion = "Drone Shot"
        ),
        PromptItem(
            id = "cin_3",
            category = "Cinematic",
            title = "Deep Ocean Submersible",
            prompt = "A deep-sea exploration submarine drifting past glowing bioluminescent coral and an ancient underwater ruin, volumetric god rays, intense atmosphere, cinematic lighting",
            recommendedRatio = "16:9",
            style = "Realistic",
            cameraMotion = "Orbit"
        ),

        // Nature
        PromptItem(
            id = "nat_1",
            category = "Nature",
            title = "Emerald Waterfall Valley",
            prompt = "Majestic cascading waterfall deep in a misty tropical rainforest, emerald green moss, water droplets sparkling in morning sunlight, hyperrealistic nature documentary style",
            recommendedRatio = "9:16",
            style = "Documentary",
            cameraMotion = "Tilt"
        ),
        PromptItem(
            id = "nat_2",
            category = "Nature",
            title = "Northern Lights Aurora",
            prompt = "Vibrant emerald and violet aurora borealis dancing across a starry arctic night sky over reflective glacial fjord waters, snow-capped peaks in silhouette",
            recommendedRatio = "16:9",
            style = "Realistic",
            cameraMotion = "Static"
        ),
        PromptItem(
            id = "nat_3",
            category = "Nature",
            title = "Autumn Forest Mist",
            prompt = "Sunlight piercing through golden autumn birch trees, swirling amber leaves falling gently in slow motion, tranquil forest path, 4k ultra detailed",
            recommendedRatio = "9:16",
            style = "Cinematic",
            cameraMotion = "Tracking Shot"
        ),

        // Travel
        PromptItem(
            id = "trv_1",
            category = "Travel",
            title = "Santorini Sunset Balcony",
            prompt = "Cinematic view from a white stone terrace in Santorini overlooking the Aegean Sea, purple sunset skies, bougainvillea flowers blowing softly in Mediterranean sea breeze",
            recommendedRatio = "9:16",
            style = "Vlog",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "trv_2",
            category = "Travel",
            title = "Kyoto Bamboo Grove",
            prompt = "Atmospheric morning walk through the towering green bamboo grove of Arashiyama, dappled sunlight filtering through swaying stalks, tranquil spiritual aesthetic",
            recommendedRatio = "9:16",
            style = "Documentary",
            cameraMotion = "Handheld"
        ),
        PromptItem(
            id = "trv_3",
            category = "Travel",
            title = "Swiss Alpine Train",
            prompt = "Panoramic window view from a vintage red train gliding over a high viaduct through the snowy Swiss Alps, turquoise alpine river below, crisp mountain air",
            recommendedRatio = "16:9",
            style = "Cinematic",
            cameraMotion = "Pan Right"
        ),

        // Product Ads
        PromptItem(
            id = "prd_1",
            category = "Product Ads",
            title = "Luxury Watch Water Splash",
            prompt = "Commercial product reveal of a titanium chronograph luxury watch with crystal water droplets exploding in ultra slow motion, dark matte studio background, rim lighting",
            recommendedRatio = "9:16",
            style = "Product Commercial",
            cameraMotion = "Orbit"
        ),
        PromptItem(
            id = "prd_2",
            category = "Product Ads",
            title = "Wireless Earbuds Levitation",
            prompt = "Minimalist futuristic wireless earbuds floating effortlessly with sound waves visualized as gentle glowing particles, pristine satin gradient backdrop",
            recommendedRatio = "1:1",
            style = "Product Commercial",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "prd_3",
            category = "Product Ads",
            title = "Perfume Bottle in Flower Petals",
            prompt = "Crystal glass perfume bottle surrounded by floating rose and orchid petals in mid-air, golden champagne liquid refracting soft morning light, ultra luxury ad",
            recommendedRatio = "9:16",
            style = "Product Commercial",
            cameraMotion = "Tracking Shot"
        ),

        // Fashion
        PromptItem(
            id = "fsh_1",
            category = "Fashion",
            title = "High Fashion Runway Walk",
            prompt = "Editorial runway model in an avant-garde iridescent silver silk gown flowing dramatically, spotlight against dark minimalist catwalk, vogue magazine aesthetic",
            recommendedRatio = "9:16",
            style = "Cinematic",
            cameraMotion = "Tracking Shot"
        ),
        PromptItem(
            id = "fsh_2",
            category = "Fashion",
            title = "Streetwear Neon Night",
            prompt = "Stylized youth wearing oversized urban puffer jacket and chrome accessories standing under flashing neon signs, reflective wet asphalt, film portrait",
            recommendedRatio = "9:16",
            style = "Cyberpunk",
            cameraMotion = "Handheld"
        ),

        // Food
        PromptItem(
            id = "fod_1",
            category = "Food",
            title = "Sizzling Wagyu Steak",
            prompt = "Macro slow motion shot of premium marbled wagyu steak searing on a blazing iron grill, rosemary sprig smoking, molten garlic butter basting, mouth-watering gourmet commercial",
            recommendedRatio = "16:9",
            style = "Product Commercial",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "fod_2",
            category = "Food",
            title = "Espresso Pour & Crema",
            prompt = "Rich dark espresso pouring smoothly into a transparent glass cup, thick golden crema swirling, rising fragrant steam, cozy artisan cafe atmosphere",
            recommendedRatio = "9:16",
            style = "Product Commercial",
            cameraMotion = "Tilt"
        ),

        // Architecture
        PromptItem(
            id = "arc_1",
            category = "Architecture",
            title = "Futuristic Glass Villa",
            prompt = "Architectural drone tour of a cliffside modernist cantilever villa made of glass and raw concrete, infinity pool blending into ocean horizon, warm sunset illumination",
            recommendedRatio = "16:9",
            style = "Realistic",
            cameraMotion = "Drone Shot"
        ),
        PromptItem(
            id = "arc_2",
            category = "Architecture",
            title = "Gothic Cathedral Light Beams",
            prompt = "Soaring arched ceilings of an ancient stone gothic cathedral, majestic light beams streaming through stained glass windows illuminating floating dust motes",
            recommendedRatio = "9:16",
            style = "Cinematic",
            cameraMotion = "Tilt"
        ),

        // Characters
        PromptItem(
            id = "chr_1",
            category = "Characters",
            title = "Cybernetic Samurai",
            prompt = "Intense portrait of a lone cybernetic warrior with glowing teal optics, wearing weathered carbon-fiber armor and a bamboo hat in a torrential rainstorm, blade drawn",
            recommendedRatio = "9:16",
            style = "Cyberpunk",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "chr_2",
            category = "Characters",
            title = "Stargazing Astronaut",
            prompt = "An astronaut standing on the lunar surface gazing up at the illuminated Earth rising above craters, detailed reflection in golden helmet visor, deep cosmic tranquility",
            recommendedRatio = "1:1",
            style = "Cinematic",
            cameraMotion = "Pull Out"
        ),

        // Anime
        PromptItem(
            id = "anm_1",
            category = "Anime",
            title = "Makoto Shinkai Cloudscape",
            prompt = "Breathtaking anime style sky with towering cumulonimbus clouds, vibrant cobalt blue and pastel orange sunset, cherry blossom petals swirling across the screen, high emotion",
            recommendedRatio = "16:9",
            style = "Anime",
            cameraMotion = "Pan Right"
        ),
        PromptItem(
            id = "anm_2",
            category = "Anime",
            title = "Mecha Awakening",
            prompt = "Colossal anime mecha powering up in an underground hangar, hydraulic steam releases, neon power lines surging to life, epic scale and hand-drawn animation feeling",
            recommendedRatio = "16:9",
            style = "Anime",
            cameraMotion = "Tilt"
        ),

        // Fantasy
        PromptItem(
            id = "fan_1",
            category = "Fantasy",
            title = "Floating Island Citadel",
            prompt = "Floating mythical islands bound by ancient crystal chains, enchanted waterfalls tumbling into the endless clouds, dragons soaring in the golden distance, fantasy masterpiece",
            recommendedRatio = "16:9",
            style = "Fantasy",
            cameraMotion = "Drone Shot"
        ),
        PromptItem(
            id = "fan_2",
            category = "Fantasy",
            title = "Enchanted Glowing Forest",
            prompt = "Magical mystical woodland with luminous giant mushrooms, glowing butterflies fluttering between weeping willow branches, tranquil spiritual fairy realm",
            recommendedRatio = "9:16",
            style = "Fantasy",
            cameraMotion = "Tracking Shot"
        ),

        // Social Media
        PromptItem(
            id = "soc_1",
            category = "Social Media",
            title = "Viral POV Coffee Routine",
            prompt = "First person POV pouring foamed oat milk into matcha latte with intricate heart art, clean aesthetic modern kitchen counter, crisp morning daylight, TikTok aesthetic",
            recommendedRatio = "9:16",
            style = "Vlog",
            cameraMotion = "Handheld"
        ),
        PromptItem(
            id = "soc_2",
            category = "Social Media",
            title = "Supercar Night Cruise",
            prompt = "Sleek matte black hypercar gliding through city tunnels under orange sodium lights, motion blur trails, rear exhaust flames, high energy reel aesthetic",
            recommendedRatio = "9:16",
            style = "Cinematic",
            cameraMotion = "Tracking Shot"
        ),

        // YouTube
        PromptItem(
            id = "yt_1",
            category = "YouTube",
            title = "Epic Cinematic Intro",
            prompt = "Dramatic 3D metallic logo shattering through frozen ice crystals into sparks and volumetric fog, cinematic boom effect, high production value channel intro",
            recommendedRatio = "16:9",
            style = "3D",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "yt_2",
            category = "YouTube",
            title = "Lofi Study Window Rain",
            prompt = "Cozy bedroom window overlooking a rainy city street at dusk, warm desk lamp, steaming mug, sleeping cat on windowsill, comforting nostalgic anime vibe",
            recommendedRatio = "16:9",
            style = "Anime",
            cameraMotion = "Static"
        ),

        // Short Films
        PromptItem(
            id = "flm_1",
            category = "Short Films",
            title = "Film Noir Detective Silhouette",
            prompt = "A classic fedora-wearing detective standing under a flickering street lamp on a foggy cobbled quay, smoke rising from a cigarette, high-contrast monochrome cinematography",
            recommendedRatio = "16:9",
            style = "Cinematic",
            cameraMotion = "Slow Push In"
        ),
        PromptItem(
            id = "flm_2",
            category = "Short Films",
            title = "Deep Space Discovery",
            prompt = "A solitary cosmic explorer floating outside an orbital research station touching an alien monolith that pulses with golden runes, 2001 Space Odyssey homage",
            recommendedRatio = "16:9",
            style = "Cinematic",
            cameraMotion = "Orbit"
        )
    )
}
