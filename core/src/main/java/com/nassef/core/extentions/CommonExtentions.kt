package com.nassef.core.extentions

import android.text.Editable
import android.util.Base64
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun Date.getCurrentDateTimeUTC(pattern: String): String {
    val dateFormatGmt = SimpleDateFormat(pattern, Locale.US)
    dateFormatGmt.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormatGmt.format(this)
}

fun Double.formatDecimalByPattern(pattern: String = "#00.00"): String {
    val decimalFormat = DecimalFormat(pattern, DecimalFormatSymbols(Locale.US))
    return decimalFormat.format(this)
}

fun ByteArray.base64Encode(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

fun String.base64Decode(): ByteArray {
    return Base64.decode(this, Base64.NO_WRAP)
}

fun ByteArray.clear() {
    fill(0)
}

fun CharArray.clear() {
    fill('0')
}

fun Editable.toCharArray(): CharArray {
    val chars = CharArray(length)
    for (i in indices) {
        chars[i] = get(i)
    }
    return chars
}

fun Long.getRemainingTime(): String {
    val hours: Int = ((this / 1000) / 3600).toInt()
    val minutes: Int = (((this / 1000) % 3600) / 60).toInt()
    val seconds: Int = ((this / 1000) % 60).toInt()

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}