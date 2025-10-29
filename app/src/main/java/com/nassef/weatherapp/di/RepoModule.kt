package com.nassef.weatherapp.di

import com.nassef.data.local.ArticleDao
import com.nassef.data.network.ArticlesApi
import com.nassef.data.repository.ArticlesRepositoryImp
import com.nassef.domain.repository.IarticlesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun provideArticlesRepo(articlesApi: ArticlesApi, articleDao: ArticleDao): IarticlesRepository =
        ArticlesRepositoryImp(articlesApi, articleDao)

}