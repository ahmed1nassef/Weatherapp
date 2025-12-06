package com.nassef.weatherapp.mappers

import com.nassef.domain.entities.Article
import com.nassef.weatherapp.utils.TimeFormatter
import javax.inject.Inject

class ArticleUiMapper @Inject constructor(
    private val timeFormatter: TimeFormatter
) {

    fun toUiModel(
        article: Article,
        bookmarkedArticles: List<Article>
    ): ArticleUiModel {
        val bookmarkedArticle = bookmarkedArticles.firstOrNull { it.url == article.url }
        val isBookmarked = bookmarkedArticle != null
        val bookmarkedId = bookmarkedArticle?.id ?: article.id

        val formattedDate = article.publishedAt?.let {
            timeFormatter.convertIsoToRelativeTime(isoTime = it)
        } ?: "Unknown Time"

        return ArticleUiModel(
            article = article.copy(
                id = bookmarkedId,
                isBookMarked = isBookmarked
            ),
            formattedDate = formattedDate,
            isBookmarked = isBookmarked
        )
    }

    fun toUiModelList(
        articles: List<Article>,
        bookmarkedArticles: List<Article>
    ): List<ArticleUiModel> {
        return articles.map { toUiModel(it, bookmarkedArticles) }
    }
}
