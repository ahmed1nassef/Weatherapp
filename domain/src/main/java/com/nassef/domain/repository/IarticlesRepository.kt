package com.nassef.domain.repository

import com.nassef.domain.entities.ArticleX
import com.nassef.domain.utilities.Results
import kotlinx.coroutines.flow.Flow

interface IarticlesRepository {
    suspend fun getAllArticles(country : String) : Results<List<ArticleX>>
    suspend fun getArticleByKeyWord(searchTxt :String) : Results<List<ArticleX>>
    fun getSavedArticles() : Flow<ArticleX>
    suspend fun upsertArticle(article: ArticleX)
    suspend fun upsertAllArticle(article: List<ArticleX>)
    suspend fun deleteArticleById(id :Int)
    suspend fun deleteArticle(article: ArticleX)
}