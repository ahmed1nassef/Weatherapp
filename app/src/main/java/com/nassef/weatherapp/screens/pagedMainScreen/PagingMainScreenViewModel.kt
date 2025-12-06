package com.nassef.weatherapp.screens.pagedMainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Article
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.deleteArticle.interactor.DeleteArticleByIdUC
import com.nassef.domain.features.getArticles.interactor.GetArticlesUC
import com.nassef.domain.features.getArticles.interactor.GetPagingArticlesUC
import com.nassef.domain.features.getArticles.model.ArticleRequest
import com.nassef.domain.features.getBookMarks.interecator.GetBookMarksUC
import com.nassef.domain.features.saveArticle.interactor.SaveArticleUC
import com.nassef.domain.features.searchArticls.interactor.SearchArticleUC
import com.nassef.domain.features.searchArticls.model.ArticleSearchRequest
import com.nassef.domain.utilities.WhileUiSubscribed
import com.nassef.domain.utilities.defaultCategory
import com.nassef.weatherapp.mappers.ArticleUiMapper
import com.nassef.weatherapp.mappers.ArticleUiModel
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<ArticleUiModel> = emptyList(),
    val error: String? = null,
    val category: Int = defaultCategory,
    val isBookMarksLoaded: Boolean = false
)

@HiltViewModel
class PagingMainScreenViewMode @Inject constructor(
    private val useCase: GetArticlesUC,
    private val pagingUseCase: GetPagingArticlesUC,
    private val saveUseCase: SaveArticleUC,
    private val searchArticleUseCase: SearchArticleUC,
    private val bookMarkedArticleUseCase: GetBookMarksUC,
    private val deleteUseCase: DeleteArticleByIdUC,
    private val uiManager: UiManager,
    private val articleUiMapper: ArticleUiMapper
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _articlesList = MutableStateFlow<List<Article>>(emptyList())

    private val _bookMarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val _category = MutableStateFlow<Int>(defaultCategory)
    private var _searchJob: Job? = null
    val articles: Flow<PagingData<List<ArticleUiModel>>> =
        pagingUseCase.invoke(ArticleRequest(countryCode = "us"))
            .cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { articlesEntity ->
                    articlesEntity.articles.map {
                        articleUiMapper.toUiModel(
                            it,
                            _bookMarkedArticles.value
                        )
                    }

                }
            }


    val uiState: StateFlow<UiState> =
        combine(
            _isLoading,
            _articlesList,
            _bookMarkedArticles,
            _error,
            _category
        ) { isLoading, rawArticles, bookmarkedArticles, error, category ->
            val uiArticles = articleUiMapper.toUiModelList(rawArticles, bookmarkedArticles)
            UiState(
                isLoading = isLoading,
                articles = uiArticles,
                error = error,
                category = category,
                isBookMarksLoaded = bookmarkedArticles.isNotEmpty()
            )
        }.combine(_isRefreshing) { uiStateValue, isRefreshing ->
            UiState(
                isLoading = uiStateValue.isLoading,
                isRefreshing = isRefreshing,
                articles = uiStateValue.articles,
                error = uiStateValue.error,
                category = uiStateValue.category,
                isBookMarksLoaded = uiStateValue.isBookMarksLoaded
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
                    _articlesList.value = it.model.articles
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
//                    if (_articlesList.value.isEmpty())
//                        getAllArticles()
                }
            }
        }
    }


    fun addArticleToBookMarks(articleUiModel: ArticleUiModel) {
        val articleToSave = articleUiModel.article.copy(isBookMarked = true)
        saveUseCase.invoke(scope = viewModelScope, articleToSave) {
            when (it) {
                is Resource.Failure -> {
                    handleError(it)
                }

                is Resource.Progress -> _isLoading.value = it.loading
                is Resource.Success -> sendMessage(it.model)
            }
        }
    }

    fun deleteBookMarkedArticle(articleUiModel: ArticleUiModel) {
        deleteUseCase.invoke(scope = viewModelScope, body = articleUiModel.article.id) {
            when (it) {
                is Resource.Failure -> handleError(it)
                is Resource.Progress<*> -> _isLoading.value = it.loading
                is Resource.Success<*> -> sendMessage("deleted successfully")
            }
        }

    }

    fun toggleArticleBookMark(articleUiModel: ArticleUiModel) {
        if (articleUiModel.isBookmarked)
            deleteBookMarkedArticle(articleUiModel)
        else
            addArticleToBookMarks(articleUiModel)
    }

    private fun handleError(failure: Resource.Failure) {
        _error.value = failure.exception.message
        _error.value?.let { sendMessage(it) }
    }

    fun searchArticle(searchQuery: String) {
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            delay(400) // Increased debounce delay for better performance
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

    fun searchCategory(searchQuary: String, categoryId: Int) {
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