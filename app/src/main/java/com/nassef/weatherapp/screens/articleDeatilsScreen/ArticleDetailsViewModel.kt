package com.nassef.weatherapp.screens.articleDeatilsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Article
import com.nassef.domain.features.getBookMarks.interecator.GetBookMarksUC
import com.nassef.domain.features.searchArticls.interactor.SearchArticleUC
import com.nassef.domain.features.searchArticls.model.ArticleSearchRequest
import com.nassef.domain.utilities.WhileUiSubscribed
import com.nassef.weatherapp.navigation.WeatherDistArgs
import com.nassef.weatherapp.utils.TimeFormatter
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.net.URLDecoder
import javax.inject.Inject

data class DetailsScreenUi(
    val isLoading: Boolean = false,
    val error: String? = null,
    val article: List<Article> = emptyList(),
    val currentArticle: Article? = null
)

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    useCase: GetBookMarksUC,
    remoteUseCase: SearchArticleUC,
    val uiManager: UiManager,
    private val timeFormatter: TimeFormatter,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val _isLoading = MutableStateFlow(false)
    val _error = MutableStateFlow<String?>(null)
    val _article = MutableStateFlow<List<Article>>(emptyList())
    val _currentArticle = MutableStateFlow<Article?>(null)

    val uiState = combine(
        _isLoading,
        _error,
        _article,
        _currentArticle
    ) { isLoading, error, article, currentArticle ->
        DetailsScreenUi(isLoading, error, article, currentArticle)
    }.stateIn(viewModelScope, WhileUiSubscribed, DetailsScreenUi())

    init {
        val articleId = savedStateHandle.get<Int>(WeatherDistArgs.ARTICLE_ID_NAV_ARG)
        val articleUrl = savedStateHandle.get<String>(WeatherDistArgs.ARTICLE_URL_NAV_ARG)
        useCase.invoke(scope = viewModelScope) {
            when (it) {
                is Resource.Failure -> _error.value = it.exception.message
                is Resource.Progress<*> -> _isLoading.value = it.loading
                is Resource.Success -> {
                    _article.value = it.model
                    getSelectedArticle(articleId, remoteUseCase, articleUrl)
                }
            }
        }
    }

    private fun getSelectedArticle(
        articleId: Int?,
        remoteUseCase: SearchArticleUC,
        articleUrl: String?
    ) {
        _currentArticle.value = articleId?.run {
            _article.value.firstOrNull {
                it.id == this
            }
        }
        _currentArticle.value ?: articleUrl?.run {
            val url = URLDecoder.decode(this, "UTF-8")
            remoteUseCase.invoke(scope = viewModelScope, ArticleSearchRequest(url)) {
                when (it) {
                    is Resource.Failure -> _error.value = it.exception.message
                    is Resource.Progress<*> -> _isLoading.value = it.loading
                    is Resource.Success -> _currentArticle.value =
                        it.model.articles.firstOrNull { remoteArticle ->
                            remoteArticle.url == articleUrl
                        }
                }
            }
        }
    }

    fun getBookMarkedArticle(id: Int) {
        val bookMarkedArticle = _article.value.firstOrNull {
            it.id == id
        }
        val publishedAt = bookMarkedArticle?.publishedAt?.let {
            timeFormatter.convertIsoToRelativeTime(it)
        }
        _currentArticle.value = bookMarkedArticle?.copy(publishedAt = publishedAt)
    }

    fun sendMsg(msg: String) {
        uiManager.sendMessage(msg)
    }
}