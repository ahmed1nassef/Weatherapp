package com.nassef.weatherapp.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Article
import com.nassef.domain.features.deleteArticle.interactor.DeleteArticleByIdUC
import com.nassef.domain.features.getArticles.interactor.GetArticlesUC
import com.nassef.domain.features.getArticles.model.ArticleRequest
import com.nassef.domain.features.getBookMarks.interecator.GetBookMarksUC
import com.nassef.domain.features.saveArticle.interactor.SaveArticleUC
import com.nassef.domain.features.searchArticls.interactor.SearchArticleUC
import com.nassef.domain.features.searchArticls.model.ArticleSearchRequest
import com.nassef.domain.utilities.WhileUiSubscribed
import com.nassef.domain.utilities.defaultCategory
import com.nassef.weatherapp.utils.TimeFormatter
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val category: Int = defaultCategory
)

@HiltViewModel
class UpgradedMainScreenViewModel @Inject constructor(
    private val useCase: GetArticlesUC,
    private val saveUseCase: SaveArticleUC,
    private val searchArticleUseCase: SearchArticleUC,
    private val bookMarkedArticleUseCase: GetBookMarksUC,
    private val deleteUseCase: DeleteArticleByIdUC,
    private val uiManager: UiManager,
    private val timeFormatter: TimeFormatter
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _articlesList = MutableStateFlow<List<Article>>(emptyList())

    private val _bookMarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val _category = MutableStateFlow<Int>(defaultCategory)
    private var _searchJob: Job? = null

    val uiState: StateFlow<UiState> =
        combine(
            _isLoading,
            _articlesList,
            _bookMarkedArticles,
            _error,
            _category
        ) { isLoading, rawArticles, bookmarkedArticles, error, category ->
            val updatedList: List<Article> = rawArticles.map { articleX ->
                mapToUiDisplayedArticle(articleX, true)
            }
            UiState(
                isLoading = isLoading,
//                isRefreshing = _isLoading.value,
                articles = updatedList,
                error = error,
                category = category
            )
        }.combine(_isRefreshing) { uiStateValue, isRefreshing ->
            UiState(
                isLoading = uiStateValue.isLoading,
                isRefreshing = isRefreshing,
                articles = uiStateValue.articles,
                error = uiStateValue.error,
                category = uiStateValue.category
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = UiState(isLoading = true)
        )

    init {
        getBookMarkedArticles()
    }

    private fun getAllArticles() {
        useCase.invoke(scope = viewModelScope, body = ArticleRequest(countryCode = "us")) {
            when (it) {
                is Resource.Failure -> _error.value = it.exception.message
                is Resource.Progress -> {
                    _isLoading.value = it.loading
                    if (_isRefreshing.value)
                        _isRefreshing.value = it.loading
                }

                is Resource.Success -> {

                    _articlesList.value = it.model.articles.map { article ->
                        /*article.apply {
                            if (_bookMarkedArticles.value.isNotEmpty())
//                                handleBookMarkedArticles(this)
                        }*/
                        mapToUiDisplayedArticle(article, false)

                    }
                }
            }
        }
    }

    private fun getBookMarkedArticles() {
        bookMarkedArticleUseCase.invoke(scope = viewModelScope, body = null) {
            when (it) {
                is Resource.Failure -> _error.value = it.exception.message
                is Resource.Progress -> _isLoading.value = it.loading
                is Resource.Success -> {
                    _bookMarkedArticles.value = it.model
                    if (_articlesList.value.isEmpty())
                        getAllArticles()
                }
            }
        }
    }

    /*fun handleBookMarkedArticles(article: Article) {
        _bookMarkedArticles.value.firstOrNull {
            it.url == article.url
        }?.run {
            article.id = this.id
            article.isBookMarked = true
        }
    }*/

    fun mapToUiDisplayedArticle(article: Article, isFormatDateRequired: Boolean): Article {
        val bookMarkedArticle = bookMarkedArticle(article)

        if (bookMarkedArticle == article)
            return article

        var formatedDate = article.publishedAt
        if (isFormatDateRequired)
            formatedDate = article.publishedAt?.let {
                timeFormatter.convertIsoToRelativeTime(isoTime = it)
            } ?: "Unknown Time"

        val bookMarkedId = bookMarkedArticle?.id ?: article.id
        val isArticleBookMarked = bookMarkedArticle?.isBookMarked ?: article.isBookMarked

        return article.copy(
            id = bookMarkedId,
            isBookMarked = isArticleBookMarked,
            publishedAt = formatedDate
        )
    }

    private fun bookMarkedArticle(article: Article): Article? =
        _bookMarkedArticles.value.firstOrNull {
            it.url == article.url
        }

    fun addArticleToBookMarks(article: Article) {
        val originalArticle: Article? = _articlesList.value.firstOrNull {
            it.url == article.url
        }?.copy(isBookMarked = true)
//        originalArticle?.isBookMarked = true
        saveUseCase.invoke(scope = viewModelScope, originalArticle) {
            when (it) {
                is Resource.Failure -> {
                    handleError(it)
                }

                is Resource.Progress -> _isLoading.value = it.loading
                is Resource.Success -> sendMessage(it.model)
            }
        }
    }

    fun deleteBookMarkedArticle(article: Article) {
        deleteUseCase.invoke(scope = viewModelScope, body = article.id) {
            when (it) {
                is Resource.Failure -> handleError(it)
                is Resource.Progress<*> -> _isLoading.value = it.loading
                is Resource.Success<*> -> sendMessage("deleted successfully")
            }
        }

    }

    fun toggleArticleBookMark(article: Article) {
        if (article.isBookMarked)
            deleteBookMarkedArticle(article)
        else
            addArticleToBookMarks(article)
    }

    private fun handleError(failure: Resource.Failure) {
        _error.value = failure.exception.message
        sendMessage(_error.value!!)
    }

    fun searchArticle(searchQuery: String) {
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            delay(200)
            searchArticleUseCase.invoke(
                scope = viewModelScope,
                ArticleSearchRequest(searchQuery)
            ) {
                when (it) {
                    is Resource.Failure -> handleError(it)
                    is Resource.Progress<*> -> _isLoading.value = it.loading
                    is Resource.Success -> _articlesList.value = it.model.articles
                }

            }
        }
    }

    fun refreshArticles() {
        _isRefreshing.value = true
        getAllArticles()
    }

    fun searchCategory(searchQuary: String , categoryId: Int) {
        _category.value = categoryId
        if (categoryId == defaultCategory) {
            getAllArticles()
        } else {
            searchArticle(searchQuary)
        }
    }

    fun sendMessage(msg: String) {
        uiManager.sendMessage(msg)
    }
}