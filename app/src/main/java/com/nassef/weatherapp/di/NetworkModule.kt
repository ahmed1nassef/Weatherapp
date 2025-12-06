package com.nassef.weatherapp.di

import android.os.Build
import com.nassef.core.data.repository.remote.ApiService
import com.nassef.core.data.repository.remote.RetrofitNetworkProvider
import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.getArticles.repository.remote.ArticleRemoteAs
import com.nassef.data.features.getArticles.repository.remote.IArticlesRemoteAS
import com.nassef.data.features.searchArticls.repository.remote.ISearchRemoteAS
import com.nassef.data.features.searchArticls.repository.remote.SearchRemoteAS
import com.nassef.data.network.ArticlesApi
import com.nassef.weatherapp.utils.Constants
import com.nassef.data.network.interceptors.APIKeyInterceptor
import com.nassef.data.network.interceptors.AuthInterceptor
import com.nassef.data.network.interceptors.CacheInterceptor
import com.nassef.data.network.interceptors.RetryInterceptor
import com.nassef.domain.utilities.NEWS_API_KEY_NAME
import com.nassef.weatherapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
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
            .addInterceptor(authInterceptor)
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
    @Named(NEWS_API_KEY_NAME)
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Provides
    @Singleton
    fun provideAPIKeyInterceptor(): APIKeyInterceptor = APIKeyInterceptor()
    @Provides
    @Singleton
    fun provideCacheInterceptor(): CacheInterceptor = CacheInterceptor()

    @Provides
    @Singleton
    fun provideArticleRemoteAS(provider: INetworkProvider): IArticlesRemoteAS = ArticleRemoteAs(provider)
    @Provides
    @Singleton
    fun provideSearchArticleRemoteAS(provider: INetworkProvider): ISearchRemoteAS =
        SearchRemoteAS(provider)

}