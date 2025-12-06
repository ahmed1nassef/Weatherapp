package com.nassef.weatherapp.screens.bookMarksScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Article
import com.nassef.domain.features.deleteArticle.interactor.DeleteArticleByIdUC
import com.nassef.domain.features.getBookMarks.interecator.GetBookMarksUC
import com.nassef.domain.utilities.WhileUiSubscribed
import com.nassef.domain.utilities.defaultCategory
import com.nassef.weatherapp.mappers.ArticleUiMapper
import com.nassef.weatherapp.mappers.ArticleUiModel
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<ArticleUiModel> = emptyList(),
    val error: String? = null,
    val category: Int = defaultCategory,
    val isArticleAdded: Boolean = false,
    val isArticleDeleted: Boolean = false
)

@HiltViewModel
class BookMarksViewModel @Inject constructor(
    private val useCase: GetBookMarksUC,
    private val deleteUseCase: DeleteArticleByIdUC,
    private val uiManager: UiManager,
    private val articleUiMapper: ArticleUiMapper
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _isDeleted = MutableStateFlow(false)
    private val _articlesList = MutableStateFlow<List<Article>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val _category = MutableStateFlow<Int>(defaultCategory)
    private var _searchJob: Job? = null

    val uiState = combine(
        _isLoading, _isRefreshing, _articlesList, _error, _category
    ) { isLoading, isRefreshing, articles, error, category ->
        val uiArticles = articleUiMapper.toUiModelList(articles, articles)
        UiState(
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            articles = uiArticles,
            error = error,
            category = category,
        )
    }.stateIn(scope = viewModelScope, started = WhileUiSubscribed, initialValue = UiState())

    init {
        getBookMarks()
    }

    private fun getBookMarks() {
        useCase.invoke(scope = viewModelScope, null) {
            when (it) {
                is Resource.Failure -> {
                    _error.value = it.exception.message
                    sendMsg(_error.value!!)
                }

                is Resource.Progress<*> -> _isLoading.value = it.loading
                is Resource.Success -> _articlesList.value = it.model
            }
        }
    }

    fun deleteArticleById(id: Int) {
        deleteUseCase.invoke(scope = viewModelScope, id) {
            when (it) {
                is Resource.Failure -> {
                    _error.value = it.exception.message
                    sendMsg(_error.value!!)
                }

                is Resource.Progress<*> -> _isLoading.value = it.loading
                is Resource.Success -> _isDeleted.value = it.model
            }
        }
    }

    fun sendMsg(msg: String) {
        uiManager.sendMessage(msg)
    }

}