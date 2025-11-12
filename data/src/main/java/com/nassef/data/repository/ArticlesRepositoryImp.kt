package com.nassef.data.repository

import com.nassef.data.local.ArticleDao
import com.nassef.data.network.ArticlesApi
import com.nassef.domain.entities.Article
import com.nassef.domain.repository.IarticlesRepository
import com.nassef.domain.utilities.Results
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImp(val articlesApi: ArticlesApi, val articleDao: ArticleDao) :
    IarticlesRepository {
    override suspend fun getAllArticles(country: String): Results<List<Article>> {
        var articleResults: Results<List<Article>>
        try {
            val response = articlesApi.getArticles(country)
            articleResults = Results.Success(response.articles)
        } catch (e: Exception) {
            articleResults = Results.Error(e, e.message, null)
        }
        return articleResults
    }

    override suspend fun getArticleByKeyWord(searchTxt: String): Results<List<Article>> {
        var articleResults: Results<List<Article>>
        try {
            val response = articlesApi.searchArticles(searchTxt)
            articleResults = Results.Success(response.articles)
        } catch (e: Exception) {
            articleResults = Results.Error(e, e.message, null)
        }
        return articleResults
    }

    override fun getSavedArticles(): Flow<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertArticle(article: Article) {
        articleDao.upsertArticle(article)
    }

    override suspend fun upsertAllArticle(article: List<Article>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArticleById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArticle(article: Article) {
        TODO("Not yet implemented")
    }
}