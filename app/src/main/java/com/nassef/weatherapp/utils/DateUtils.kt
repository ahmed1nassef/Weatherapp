package com.nassef.weatherapp.utils

import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeFormatter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Converts an ISO 8601 timestamp string to a human-readable relative time string.
     */
    fun convertIsoToRelativeTime(isoTime: String): String {
        return try {
            // 1. Parse the ISO 8601 string to an Instant
            val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.parse(isoTime)
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            // 2. Convert Instant to milliseconds for DateUtils
            val timeInMillis = instant.toEpochMilli()

            // 3. Get the current time in milliseconds
            val now = System.currentTimeMillis()

            // 4. Use DateUtils to generate the relative time string
            DateUtils.getRelativeTimeSpanString(
                timeInMillis,
                now,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()

        } catch (e: Exception) {
            // Log the error (optional)
            // Log.e("TimeFormatter", "Error parsing time: $isoTime", e)
            ""
        }
    }
}