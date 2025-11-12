package com.nassef.domain.utilities

import com.nassef.domain.R
import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 */
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
val categoriesList = listOf(
    R.string.all_category,
    R.string.business_category,
    R.string.technology_category,
    R.string.health_category, R.string.entertainment_category,
    R.string.games_category
)
val defaultCategory: Int = R.string.all_category
const val NEWS_API_KEY_NAME = "NewsApiKey"