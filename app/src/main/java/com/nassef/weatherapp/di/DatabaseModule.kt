package com.nassef.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.nassef.data.local.ArticleDao
import com.nassef.data.local.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideArticlesDao(articleDatabase: ArticleDatabase): ArticleDao =
        articleDatabase.articleDao()

    @Provides
    @Singleton
    fun provideArticlesDatabase(@ApplicationContext context: Context): ArticleDatabase =
        Room.databaseBuilder(
            context,
            ArticleDatabase::class.java, "article_database"
        ).fallbackToDestructiveMigration(true).build()

}