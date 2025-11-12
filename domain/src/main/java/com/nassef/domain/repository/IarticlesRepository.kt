package com.nassef.domain.repository

import com.nassef.domain.entities.Article
import com.nassef.domain.utilities.Results
import kotlinx.coroutines.flow.Flow

interface IarticlesRepository {
    suspend fun getAllArticles(country : String) : Results<List<Article>>
    suspend fun getArticleByKeyWord(searchTxt :String) : Results<List<Article>>
    fun getSavedArticles() : Flow<Article>
    suspend fun upsertArticle(article: Article)
    suspend fun upsertAllArticle(article: List<Article>)
    suspend fun deleteArticleById(id :Int)
    suspend fun deleteArticle(article: Article)
}