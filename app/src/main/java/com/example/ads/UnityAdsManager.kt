package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsLoadOptions
import com.unity3d.ads.UnityAdsShowOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UnityAdsManager {
    private const val TAG = "UnityAdsManager"

    // Unity Monetization Game ID
    const val GAME_ID = "800367852"

    // Unity Android Ad Unit Placements
    const val BANNER_PLACEMENT_ID = "Banner_Android"
    const val INTERSTITIAL_PLACEMENT_ID = "Interstitial_Android"
    const val REWARDED_PLACEMENT_ID = "Rewarded_Android"

    // Test mode: default to true if needed, or false for production
    var isTestMode = false

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _isInterstitialLoaded = MutableStateFlow(false)
    val isInterstitialLoaded: StateFlow<Boolean> = _isInterstitialLoaded.asStateFlow()

    private val _isRewardedLoaded = MutableStateFlow(false)
    val isRewardedLoaded: StateFlow<Boolean> = _isRewardedLoaded.asStateFlow()

    private val _lastStatus = MutableStateFlow<String>("Initializing...")
    val lastStatus: StateFlow<String> = _lastStatus.asStateFlow()

    fun initialize(context: Context, testMode: Boolean = false) {
        isTestMode = testMode
        if (UnityAds.isInitialized) {
            _isInitialized.value = true
            _lastStatus.value = "Initialized"
            loadInterstitial(context)
            loadRewarded(context)
            return
        }

        UnityAds.initialize(
            context.applicationContext,
            GAME_ID,
            isTestMode,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    Log.d(TAG, "Unity Ads Initialization Complete! (testMode=$isTestMode)")
                    _isInitialized.value = true
                    _lastStatus.value = "Active (testMode=$isTestMode)"
                    loadInterstitial(context)
                    loadRewarded(context)
                }

                override fun onInitializationFailed(
                    error: UnityAds.UnityAdsInitializationError?,
                    message: String?
                ) {
                    val err = "Init failed: $error - $message"
                    Log.w(TAG, err)
                    _isInitialized.value = false
                    _lastStatus.value = err
                }
            }
        )
    }

    fun loadInterstitial(context: Context) {
        if (!UnityAds.isInitialized) {
            Log.d(TAG, "Cannot load interstitial: SDK not initialized yet")
            return
        }

        UnityAds.load(
            INTERSTITIAL_PLACEMENT_ID,
            UnityAdsLoadOptions(),
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String?) {
                    Log.d(TAG, "Interstitial loaded: $placementId")
                    _isInterstitialLoaded.value = true
                    _lastStatus.value = "Interstitial Ready"
                }

                override fun onUnityAdsFailedToLoad(
                    placementId: String?,
                    error: UnityAds.UnityAdsLoadError?,
                    message: String?
                ) {
                    Log.w(TAG, "Interstitial load failed on $placementId: $error - $message")
                    _isInterstitialLoaded.value = false
                    _lastStatus.value = "Load error: $message"
                }
            }
        )
    }

    fun loadRewarded(context: Context) {
        if (!UnityAds.isInitialized) {
            Log.d(TAG, "Cannot load rewarded: SDK not initialized yet")
            return
        }

        UnityAds.load(
            REWARDED_PLACEMENT_ID,
            UnityAdsLoadOptions(),
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String?) {
                    Log.d(TAG, "Rewarded ad loaded: $placementId")
                    _isRewardedLoaded.value = true
                    _lastStatus.value = "Rewarded Ready"
                }

                override fun onUnityAdsFailedToLoad(
                    placementId: String?,
                    error: UnityAds.UnityAdsLoadError?,
                    message: String?
                ) {
                    Log.w(TAG, "Rewarded load failed on $placementId: $error - $message")
                    _isRewardedLoaded.value = false
                    _lastStatus.value = "Load error: $message"
                }
            }
        )
    }

    /**
     * Shows interstitial ad safely.
     * If the ad is not ready, immediately invokes onAdClosed() without blocking the user.
     */
    fun showInterstitial(
        activity: Activity,
        onAdClosed: () -> Unit = {}
    ) {
        if (!UnityAds.isInitialized) {
            Log.d(TAG, "SDK not initialized, proceeding without ad")
            onAdClosed()
            return
        }

        if (!_isInterstitialLoaded.value) {
            Log.d(TAG, "Interstitial not ready yet, proceeding and requesting load in background")
            loadInterstitial(activity)
            onAdClosed()
            return
        }

        UnityAds.show(
            activity,
            INTERSTITIAL_PLACEMENT_ID,
            UnityAdsShowOptions(),
            object : IUnityAdsShowListener {
                override fun onUnityAdsShowFailure(
                    placementId: String?,
                    error: UnityAds.UnityAdsShowError?,
                    message: String?
                ) {
                    Log.w(TAG, "Interstitial show failed: $placementId - $message")
                    _isInterstitialLoaded.value = false
                    loadInterstitial(activity)
                    onAdClosed()
                }

                override fun onUnityAdsShowStart(placementId: String?) {
                    Log.d(TAG, "Interstitial show started: $placementId")
                }

                override fun onUnityAdsShowClick(placementId: String?) {
                    Log.d(TAG, "Interstitial clicked: $placementId")
                }

                override fun onUnityAdsShowComplete(
                    placementId: String?,
                    state: UnityAds.UnityAdsShowCompletionState?
                ) {
                    Log.d(TAG, "Interstitial show completed: $placementId, state: $state")
                    _isInterstitialLoaded.value = false
                    loadInterstitial(activity)
                    onAdClosed()
                }
            }
        )
    }

    /**
     * Shows rewarded video ad safely.
     * If the ad is not ready, alerts or gives the reward gracefully.
     */
    fun showRewarded(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdClosed: () -> Unit = {}
    ) {
        if (!UnityAds.isInitialized) {
            Log.d(TAG, "SDK not initialized, granting reward gracefully")
            onRewardEarned()
            onAdClosed()
            return
        }

        if (!_isRewardedLoaded.value) {
            Log.d(TAG, "Rewarded ad not loaded yet, triggering background load")
            loadRewarded(activity)
            android.widget.Toast.makeText(
                activity,
                "Ad is preparing... Unlocked bonus for you!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            onRewardEarned()
            onAdClosed()
            return
        }

        UnityAds.show(
            activity,
            REWARDED_PLACEMENT_ID,
            UnityAdsShowOptions(),
            object : IUnityAdsShowListener {
                override fun onUnityAdsShowFailure(
                    placementId: String?,
                    error: UnityAds.UnityAdsShowError?,
                    message: String?
                ) {
                    Log.w(TAG, "Rewarded show failed: $placementId - $message")
                    _isRewardedLoaded.value = false
                    loadRewarded(activity)
                    onRewardEarned()
                    onAdClosed()
                }

                override fun onUnityAdsShowStart(placementId: String?) {
                    Log.d(TAG, "Rewarded show started: $placementId")
                }

                override fun onUnityAdsShowClick(placementId: String?) {
                    Log.d(TAG, "Rewarded clicked: $placementId")
                }

                override fun onUnityAdsShowComplete(
                    placementId: String?,
                    state: UnityAds.UnityAdsShowCompletionState?
                ) {
                    Log.d(TAG, "Rewarded show completed: $placementId, state: $state")
                    _isRewardedLoaded.value = false
                    onRewardEarned()
                    loadRewarded(activity)
                    onAdClosed()
                }
            }
        )
    }
}
