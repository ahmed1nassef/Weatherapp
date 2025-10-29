package com.nassef.weatherapp.di

import com.nassef.domain.repository.IarticlesRepository
import com.nassef.domain.useCases.AddBookMarkedArticleUseCase
import com.nassef.domain.useCases.GetAllArticlesUseCase
import com.nassef.domain.useCases.SearchForArticleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    @Provides
    fun provideGetAllArticlesUseCase(articlesRepository: IarticlesRepository): GetAllArticlesUseCase =
        GetAllArticlesUseCase(articlesRepository)

    @Provides
    fun provideSearchForArticleUseCase(articlesRepository: IarticlesRepository): SearchForArticleUseCase =
        SearchForArticleUseCase(articlesRepository)

    @Provides
    fun provideAddBookMarkUseCase(articlesRepository: IarticlesRepository): AddBookMarkedArticleUseCase =
        AddBookMarkedArticleUseCase(articlesRepository)
}