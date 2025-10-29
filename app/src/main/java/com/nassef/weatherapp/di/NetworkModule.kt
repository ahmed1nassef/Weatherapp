package com.nassef.weatherapp.di

import com.nassef.data.network.ArticlesApi
import com.nassef.weatherapp.utils.Constants
import com.nassef.data.network.interceptors.APIKeyInterceptor
import com.nassef.data.network.interceptors.AuthInterceptor
import com.nassef.data.network.interceptors.CacheInterceptor
import com.nassef.data.network.interceptors.RetryInterceptor
import com.nassef.weatherapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(
        interceptor: APIKeyInterceptor,
        authInterceptor: AuthInterceptor,
        retryInterceptor: RetryInterceptor,
        cacheInterceptor: CacheInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
//            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
//            .addInterceptor(loggingInterceptor)
            .addInterceptor(retryInterceptor)
            .addInterceptor(cacheInterceptor)

            .build()
    }

    @Provides
    @Singleton
    fun provideArticleApi(okHttpClient: OkHttpClient): ArticlesApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ArticlesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetryInterceptor(): RetryInterceptor = RetryInterceptor(Constants.MAX_RETRY_ATTEMPTS)

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun provideAPIKeyInterceptor(): APIKeyInterceptor = APIKeyInterceptor()
    @Provides
    @Singleton
    fun provideCacheInterceptor(): CacheInterceptor = CacheInterceptor()

}