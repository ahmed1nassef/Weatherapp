package com.nassef.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
//import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class APIKeyInterceptor /*@Inject constructor()*/ : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUrl = chain.request().url
        val newUrl = currentUrl.newBuilder()
            .addQueryParameter("X-Api-Key", "176e4336f610467d8e18ec3327e0b557").build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        return chain.proceed(newRequest)
    }
}


/*
private fun getHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
    else HttpLoggingInterceptor.Level.NONE
}*/
