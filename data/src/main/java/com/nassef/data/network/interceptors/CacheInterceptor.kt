package com.nassef.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CacheInterceptor /*@Inject constructor()*/: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
//        return if (Utils.isNetworkAvailable(applicationContext)) {
        return if (true) {
            val maxAge = 60
            originalResponse.newBuilder()
                .addHeader("Cache-control", "public, max-age = $maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28 // 4 weeks
            originalResponse.newBuilder()
                .addHeader("Cache-control", "public, only-if-cache max-age = $maxStale")
                .build()
        }
    }
}