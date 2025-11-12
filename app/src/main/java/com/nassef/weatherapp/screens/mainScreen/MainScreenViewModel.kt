package com.nassef.weatherapp.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassef.domain.entities.Article
import com.nassef.domain.useCases.AddBookMarkedArticleUseCase
import com.nassef.domain.useCases.GetAllArticlesUseCase
import com.nassef.domain.useCases.SearchForArticleUseCase
import com.nassef.domain.utilities.Results
import com.nassef.domain.utilities.WhileUiSubscribed
import com.nassef.domain.utilities.defaultCategory
import com.nassef.weatherapp.utils.TimeFormatter
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val useCase: GetAllArticlesUseCase,
    private val bookMarkUseCase: AddBookMarkedArticleUseCase,
    private val searchUseCase: SearchForArticleUseCase,
    private val uiManager: UiManager,
    private val timeFormatter: TimeFormatter
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _articlesList = MutableStateFlow<List<Article>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    private val _category = MutableStateFlow<Int>(defaultCategory)
    private var _searchJob: Job? = null


    val uiState: StateFlow<UiState> =
        combine(_isLoading, _articlesList, _error, _category, _isRefreshing) {

//        if (_articlesList.value.isNullOrEmpty().not()) {
            val updatedList: List<Article> = _articlesList.value.map { articleX ->
                articleX.apply {
                    publishedAt?.apply {
                        timeFormatter.convertIsoToRelativeTime(isoTime = this)
                    }
//                    publishedAt = timeFormatter.convertIsoToRelativeTime(isoTime = publishedAt)
                }
            }
//            UiState(_isLoading.value, updatedList, _error.value)
//        }
//        UiState(isLoading = _isLoading.value, articles = updatedList, error = _error.value)
            UiState(
                isLoading = _isLoading.value,
                isRefreshing = _isRefreshing.value,
                articles = updatedList,
                error = _error.value,
                category = _category.value
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = UiState(isLoading = true)
        )

    init {
        _isLoading.value = true
        getAllArticlesOnline()
    }

    fun refreshArticles() {
        _isRefreshing.value = true
        getAllArticlesOnline()
    }

    private fun getAllArticlesOnline() {
        viewModelScope.launch {
            withContext(context = Dispatchers.IO) {
                val articlesResponse = useCase.invoke("us")
                when (articlesResponse) {
                    Results.Loading -> _isLoading.value = true
                    is Results.Success<List<Article>> -> {
                        _articlesList.value = articlesResponse.data
                        _isLoading.value = false
                        _isRefreshing.value = false
                    }

                    /* .map { article ->
                     article.apply {
                         publishedAt =
                             timeFormatter.convertIsoToRelativeTime(isoTime = publishedAt)
                     }
                 }*/

                    is Results.Error -> {
                        _error.value = articlesResponse.errorMsg
                        _isLoading.value = false
                        _isRefreshing.value = false
                    }


                }
            }

        }
    }

    fun addArticleToBookMarks(article: Article) {
        _isLoading.value = true
        val originalArticle: Article? = _articlesList.value.firstOrNull {
            it.id == article.id
        }
        hundleScope {
            originalArticle?.let {
                withContext(context = Dispatchers.IO) {
                    bookMarkUseCase.invoke(it)
                }
                uiManager.sendMessage("saved successfully")
            }
            if (originalArticle == null)
                uiManager.sendMessage("problem with saving article")
            _isLoading.value = false
        }
    }

    fun sendMessage(msg: String) {
        uiManager.sendMessage(msg)
    }

    fun searchCategory(searchQuary: Int) {
        _isLoading.value = true
        if (searchQuary == defaultCategory)
            getAllArticlesOnline()
        else {
            _category.value = searchQuary
            searchArticle(searchQuary.toString())
        }
    }

    fun searchArticle(searchQuary: String) {
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            _isLoading.value = true
            delay(200)
            val response = withContext(context = Dispatchers.IO) {
                searchUseCase.invoke(searchQuary)
            }
            when (response) {
                is Results.Error -> _error.value = response.errorMsg
                Results.Loading -> _isLoading.value = true
                is Results.Success -> _articlesList.value = response.data
            }
            _isLoading.value = false
        }


    }

    fun hundleScope(coroutineFun: suspend () -> Unit) {
        viewModelScope.launch {
            coroutineFun()
        }
    }
}