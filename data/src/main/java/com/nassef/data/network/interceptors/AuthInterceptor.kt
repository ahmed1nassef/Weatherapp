package com.nassef.data.network.interceptors

import com.nassef.domain.utilities.NEWS_API_KEY_NAME
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(@Named(NEWS_API_KEY_NAME) private val apiKey: String) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
//        val apiKey = "176e4336f610467d8e18ec3327e0b557"
        val currentRequest = chain.request().newBuilder()
        currentRequest.addHeader("Authorization", "Bearer $apiKey")

        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}