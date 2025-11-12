package com.nassef.domain.entities

data class ArticlesEntity(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)