package com.nassef.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    //insert or update articles
    @Upsert
    suspend fun upsertArticle(articleX: ArticleEntity)

    @Upsert
    suspend fun upsertAllArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles WHERE id = :articleId")
    suspend fun deleteArticleById(articleId: Int)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}