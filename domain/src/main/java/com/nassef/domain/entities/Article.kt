package com.nassef.domain.entities

data class Article(
    val articles: List<ArticleX>,
    val status: String,
    val totalResults: Int
)