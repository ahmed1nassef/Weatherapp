package com.nassef.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.math.pow

class RetryInterceptor constructor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var lastException: IOException? = null

        repeat(maxRetries) { attempt ->
            try {
                return chain.proceed(request)
            } catch (e: IOException) {
                lastException = e

                // Don't retry on the last attempt
                if (attempt < maxRetries - 1) {
                    // Exponential backoff: 1s, 2s, 4s, 8s, etc.
                    val delayMillis = (2.0.pow(attempt.toDouble()) * 1000).toLong()
                    Thread.sleep(delayMillis.coerceAtMost(10000)) // Max 10 seconds
                }
            }
        }

        // If all retries failed, throw the last exception
        throw lastException ?: IOException("Request failed after $maxRetries attempts")
    }
}