package com.nassef.weatherapp.di

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.domain.features.deleteArticle.interactor.DeleteArticleByIdUC
import com.nassef.domain.features.deleteArticle.interactor.DeleteArticleUC
import com.nassef.domain.features.deleteArticle.reposiptory.IdeleteArticletRepo
import com.nassef.domain.features.getArticles.interactor.GetArticlesUC
import com.nassef.domain.features.getArticles.interactor.GetPaginatedArticlesUC
import com.nassef.domain.features.getArticles.repository.IArticlesRepository
import com.nassef.domain.features.getBookMarks.interecator.GetBookMarksUC
import com.nassef.domain.features.getBookMarks.repository.IbookMarksRepo
import com.nassef.domain.features.saveArticle.interactor.SaveArticleUC
import com.nassef.domain.features.saveArticle.repository.ISaveArticleRepo
import com.nassef.domain.features.searchArticls.interactor.SearchArticleUC
import com.nassef.domain.features.searchArticls.repository.ISearchArticles
import com.nassef.domain.features.splashHistory.interactor.GetStartDestinationUC
import com.nassef.domain.features.splashHistory.repository.IStartDestinationRepo
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

    @Provides
    fun provideGetAllArticlesUC(
        articlesRepository: IArticlesRepository,
        errorHandler: ErrorHandler
    ): GetArticlesUC =
        GetArticlesUC(articlesRepository, errorHandler)

    @Provides
    fun provideSaveArticleUC(
        repo: ISaveArticleRepo,
        errorHandler: ErrorHandler
    ): SaveArticleUC =
        SaveArticleUC(repo, errorHandler)

    @Provides
    fun provideSearchArticlesUC(
        repo: ISearchArticles,
        errorHandler: ErrorHandler
    ): SearchArticleUC =
        SearchArticleUC(repo, errorHandler)

    @Provides
    fun provideLandingUC(
        repo: IStartDestinationRepo,
        errorHandler: ErrorHandler
    ): GetStartDestinationUC =
        GetStartDestinationUC(repo, errorHandler)

    @Provides
    fun provideBookMarksUC(
        repo: IbookMarksRepo,
        errorHandler: ErrorHandler
    ): GetBookMarksUC =
        GetBookMarksUC(repo, errorHandler)

    @Provides
    fun provideDeleteArticleByIdUC(
        repo: IdeleteArticletRepo,
        errorHandler: ErrorHandler
    ): DeleteArticleByIdUC =
        DeleteArticleByIdUC(repo, errorHandler)

    @Provides
    fun provideDeleteArticleUC(
        repo: IdeleteArticletRepo,
        errorHandler: ErrorHandler
    ): DeleteArticleUC =
        DeleteArticleUC(repo, errorHandler)

    @Provides
    fun provideGetPaginatedArticlesUC(
        repo: IArticlesRepository,
        errorHandler: ErrorHandler
    ): GetPaginatedArticlesUC =
        GetPaginatedArticlesUC(repo, errorHandler)

}