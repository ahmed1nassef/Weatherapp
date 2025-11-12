package com.nassef.weatherapp.di

import android.content.Context
import com.nassef.core.data.error.GeneralErrorHandlerImpl
import com.nassef.core.data.repository.local.keyValue.StorageKeyValueFile
import com.nassef.core.data.repository.remote.ApiService
import com.nassef.core.data.repository.remote.RetrofitNetworkProvider
import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.repository.local.IStorageKeyValue
import com.nassef.core.domain.repository.local.IStorageKeyValueFile
import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.splashHistory.repository.local.IStartDestinationLocalDS
import com.nassef.data.features.splashHistory.repository.local.StartDestinationLocalDS
import com.nassef.weatherapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModules {
    @Provides
    @Singleton
    fun provideArticleNetworkProvider(apiService: ApiService): INetworkProvider = RetrofitNetworkProvider(apiService)

    @Provides
    @Singleton
    fun provideUpgradedArticleApi(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler =
        GeneralErrorHandlerImpl()

}
@Module
@InstallIn(SingletonComponent::class)
object DataSetsModule {
    @Provides
    @Singleton
    fun provideDSfile(@ApplicationContext context: Context): IStorageKeyValueFile =
        StorageKeyValueFile(context)

    @Provides
    @Singleton
    fun provideDS(keyValueFile: IStorageKeyValueFile): IStorageKeyValue =
        keyValueFile.storageKV

    @Provides
    @Singleton
    fun provideLandingDS(storage: IStorageKeyValue): IStartDestinationLocalDS =
        StartDestinationLocalDS(storage)
}