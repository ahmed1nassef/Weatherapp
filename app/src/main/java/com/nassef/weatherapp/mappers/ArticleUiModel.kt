package com.nassef.weatherapp.mappers

import com.nassef.domain.entities.Article

data class ArticleUiModel(
    val article: Article,              // Original domain article for operations
    val formattedDate: String,         // UI-formatted date
    val isBookmarked: Boolean          // UI bookmark state
) {
    // Convenience properties for UI access
    val id: Int get() = article.id
    val author: String? get() = article.author
    val content: String? get() = article.content
    val description: String? get() = article.description
    val source get() = article.source
    val title: String get() = article.title
    val url: String get() = article.url
    val urlToImage: String? get() = article.urlToImage
    val publishedAt: String? get() = article.publishedAt
}
