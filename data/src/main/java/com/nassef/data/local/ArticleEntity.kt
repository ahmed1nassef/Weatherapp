package com.nassef.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nassef.domain.entities.Source

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = -1,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?,
    val isBookMarked: Boolean = false
)
