package com.nassef.weatherapp.di

import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.deleteArticle.repository.DeleteArticleRepository
import com.nassef.data.features.getArticles.repository.ArticlesPagingRepository
import com.nassef.data.features.getArticles.repository.ArticlesRepository
import com.nassef.data.features.getArticles.repository.remote.IArticlesRemoteAS
import com.nassef.data.features.getBookMarks.repository.BookMarksRepo
import com.nassef.data.features.saveArticle.repository.SaveArticleRepo
import com.nassef.data.features.searchArticls.repository.SearchArticlesRepository
import com.nassef.data.features.searchArticls.repository.remote.ISearchRemoteAS
import com.nassef.data.features.splashHistory.repository.StartDestinationRepo
import com.nassef.data.features.splashHistory.repository.local.IStartDestinationLocalDS
import com.nassef.data.local.ArticleDao
import com.nassef.data.network.ArticlesApi
import com.nassef.data.repository.ArticlesRepositoryImp
import com.nassef.domain.features.deleteArticle.reposiptory.IdeleteArticletRepo
import com.nassef.domain.features.getArticles.repository.IArticlesPaginRepository
import com.nassef.domain.features.getArticles.repository.IArticlesRepository
import com.nassef.domain.features.getBookMarks.repository.IbookMarksRepo
import com.nassef.domain.features.saveArticle.repository.ISaveArticleRepo
import com.nassef.domain.features.searchArticls.repository.ISearchArticles
import com.nassef.domain.features.splashHistory.repository.IStartDestinationRepo
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

    @Provides
    fun provideUpgradedArticlesRepo(remoteAS: IArticlesRemoteAS): IArticlesRepository =
        ArticlesRepository(remoteAS)

    @Provides
    fun provideUpgradedSaveArticleRepo(articleDao: ArticleDao): ISaveArticleRepo =
        SaveArticleRepo(articleDao)

    @Provides
    fun provideUpgradedSearchArticleRepo(searchAS: ISearchRemoteAS): ISearchArticles =
        SearchArticlesRepository(searchAS)

    @Provides
    fun provideLandingRepo(dataset: IStartDestinationLocalDS): IStartDestinationRepo =
        StartDestinationRepo(dataset)

    @Provides
    fun provideBookMarksRepo(dao: ArticleDao): IbookMarksRepo =
        BookMarksRepo(dao)
    @Provides
    fun provideDeleteArticleRepo(dao: ArticleDao): IdeleteArticletRepo =
        DeleteArticleRepository(dao)

    @Provides
    fun provideGetPagingArticlesRepo(provider: INetworkProvider): IArticlesPaginRepository =
        ArticlesPagingRepository(provider)

}

