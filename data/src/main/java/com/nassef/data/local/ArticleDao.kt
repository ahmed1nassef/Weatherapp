package com.nassef.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nassef.domain.entities.ArticleX
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<ArticleX>>

    //insert or update articles
    @Upsert
    suspend fun upsertArticle(articleX: ArticleX)

    @Upsert
    suspend fun upsertAllArticles(articles: List<ArticleX>)

    @Query("DELETE FROM articles WHERE id = :articleId")
    suspend fun deleteArticleById(articleId: Int)

    @Delete
    suspend fun deleteArticle(article: ArticleX)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}