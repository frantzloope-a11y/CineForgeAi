package com.example.ads

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

@Composable
fun UnityBannerAd(
    modifier: Modifier = Modifier,
    placementId: String = UnityAdsManager.BANNER_PLACEMENT_ID
) {
    val context = LocalContext.current
    val activity = context as? Activity

    if (activity == null) {
        return
    }

    var isBannerLoaded by remember { mutableStateOf(false) }

    val bannerView = remember {
        BannerView(activity, placementId, UnityBannerSize(320, 50)).apply {
            listener = object : BannerView.IListener {
                override fun onBannerLoaded(bannerView: BannerView?) {
                    Log.d("UnityBannerAd", "Banner loaded successfully: $placementId")
                    isBannerLoaded = true
                }

                override fun onBannerShown(bannerView: BannerView?) {
                    Log.d("UnityBannerAd", "Banner shown successfully: $placementId")
                    isBannerLoaded = true
                }

                override fun onBannerClick(bannerView: BannerView?) {
                    Log.d("UnityBannerAd", "Banner clicked: $placementId")
                }

                override fun onBannerFailedToLoad(
                    bannerView: BannerView?,
                    errorInfo: BannerErrorInfo?
                ) {
                    // Safe logging without crashing or breaking layout if no fill is available
                    Log.w("UnityBannerAd", "Banner no fill/failed to load (${placementId}): ${errorInfo?.errorMessage}")
                    isBannerLoaded = false
                }

                override fun onBannerLeftApplication(bannerView: BannerView?) {
                    Log.d("UnityBannerAd", "Banner left application: $placementId")
                }
            }
        }
    }

    DisposableEffect(bannerView) {
        bannerView.load()
        onDispose {
            bannerView.destroy()
        }
    }

    // Only occupy UI space when a banner has successfully loaded or is active, preventing an empty blank gap
    AnimatedVisibility(visible = isBannerLoaded) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.Black.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { bannerView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}
