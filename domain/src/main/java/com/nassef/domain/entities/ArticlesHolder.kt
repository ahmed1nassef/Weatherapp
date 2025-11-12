package com.nassef.domain.entities

data class ArticlesHolder(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)