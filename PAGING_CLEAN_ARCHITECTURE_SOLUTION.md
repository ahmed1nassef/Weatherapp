# Clean Architecture Paging Solution

## The Problem

How to implement Paging 3 while:
1. ✅ Keeping domain layer framework-independent
2. ✅ Maintaining use case pattern for consistency
3. ✅ Using BaseUseCase for loading/error handling
4. ✅ Getting benefits of Paging 3 in UI

---

## The Solution: Domain Pagination Model

### Core Principle
> **Separate pagination LOGIC (domain) from pagination FRAMEWORK (infrastructure)**

Domain defines WHAT data to get and HOW to paginate (page number, size).
Infrastructure (Paging 3) handles HOW to display it efficiently.

---

## Implementation

### 1. Domain Layer - Pure Kotlin Pagination Model

```kotlin
// domain/src/main/java/com/nassef/domain/model/PaginatedResult.kt
data class PaginatedResult<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int? = null,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)

// domain/src/main/java/com/nassef/domain/features/getArticles/model/PaginationRequest.kt
data class PaginationRequest(
    val page: Int,
    val pageSize: Int = 20,
    val filters: Map<String, String> = emptyMap()
)
```

### 2. Domain Repository Interface

```kotlin
// domain/src/main/java/com/nassef/domain/features/getArticles/repository/IArticlesRepository.kt
interface IArticlesRepository {
    // Existing method
    suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesEntity

    // New pagination method - NO Paging 3 dependency!
    suspend fun getArticlesPage(
        page: Int,
        pageSize: Int,
        country: String
    ): PaginatedResult<Article>
}
```

### 3. Domain Use Case (Extends BaseUseCase!)

```kotlin
// domain/src/main/java/com/nassef/domain/features/getArticles/interactor/GetPaginatedArticlesUC.kt
class GetPaginatedArticlesUC(
    private val repo: IArticlesRepository,
    errorHandler: ErrorHandler
) : BaseUseCase<PaginatedResult<Article>, PaginationRequest>(errorHandler) {

    override fun executeDS(body: PaginationRequest?): Flow<PaginatedResult<Article>> {
        return flow {
            requireBody(body).validateRequestContract()

            val result = repo.getArticlesPage(
                page = body!!.page,
                pageSize = body.pageSize,
                country = body.filters["country"] ?: "us"
            )

            emit(result)
        }
    }
}
```

**✅ Now you have:**
- Use case that extends BaseUseCase
- Loading/Error handling from BaseUseCase
- NO framework dependencies in domain
- Consistent with your architecture!

---

### 4. Data Layer Implementation

```kotlin
// data/src/main/java/com/nassef/data/features/getArticles/repository/ArticlesRepository.kt
class ArticlesRepository(
    private val remoteAS: IArticlesRemoteAS
) : IArticlesRepository {

    // Existing method
    override suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesEntity {
        val result = remoteAS.getAllArticles(remoteRequest)
        return ArticleMapper.dtoToDomain(result)
    }

    // New pagination method
    override suspend fun getArticlesPage(
        page: Int,
        pageSize: Int,
        country: String
    ): PaginatedResult<Article> {
        val remoteRequest = RemoteRequest(
            requestQueries = hashMapOf(
                "country" to country,
                "page" to page.toString(),
                "pageSize" to pageSize.toString()
            )
        )

        val result = remoteAS.getAllArticles(remoteRequest)
        val articlesEntity = ArticleMapper.dtoToDomain(result)

        return PaginatedResult(
            data = articlesEntity.articles,
            page = page,
            pageSize = pageSize,
            hasNextPage = articlesEntity.articles.size == pageSize,
            hasPreviousPage = page > 1
        )
    }
}
```

---

### 5. Presentation Layer - Custom PagingSource

**This is where Paging 3 framework comes in!**

```kotlin
// app/src/main/java/com/nassef/weatherapp/paging/ArticlesPagingSource.kt
class ArticlesPagingSource(
    private val useCase: GetPaginatedArticlesUC,  // ✅ Uses domain use case!
    private val country: String,
    private val coroutineScope: CoroutineScope
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1

        return try {
            var result: PaginatedResult<Article>? = null
            var error: AppException? = null

            // ✅ Use the domain use case with Resource wrapper!
            useCase.invoke(
                scope = coroutineScope,
                body = PaginationRequest(
                    page = page,
                    pageSize = params.loadSize
                )
            ) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        result = resource.model
                    }
                    is Resource.Failure -> {
                        error = resource.exception
                    }
                    is Resource.Progress -> {
                        // Loading state - handled by Paging 3
                    }
                }
            }.join()

            if (error != null) {
                LoadResult.Error(Exception(error!!.message))
            } else {
                LoadResult.Page(
                    data = result!!.data,
                    prevKey = if (result!!.hasPreviousPage) page - 1 else null,
                    nextKey = if (result!!.hasNextPage) page + 1 else null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
```

---

### 6. ViewModel - Builds Paging Flow

```kotlin
// app/src/main/java/com/nassef/weatherapp/screens/pagedMainScreen/PagingMainScreenViewModel.kt
@HiltViewModel
class PagingMainScreenViewModel @Inject constructor(
    private val getPaginatedArticlesUC: GetPaginatedArticlesUC,  // ✅ Domain use case
    private val bookMarkedArticleUseCase: GetBookMarksUC,
    private val saveUseCase: SaveArticleUC,
    private val deleteUseCase: DeleteArticleByIdUC,
    private val articleUiMapper: ArticleUiMapper,
    private val uiManager: UiManager
) : ViewModel() {

    private val _bookMarkedArticles = MutableStateFlow<List<Article>>(emptyList())

    // ✅ Build Paging flow in presentation layer
    val articlesFlow: Flow<PagingData<ArticleUiModel>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            prefetchDistance = 3
        ),
        pagingSourceFactory = {
            ArticlesPagingSource(
                useCase = getPaginatedArticlesUC,  // ✅ Inject use case
                country = "us",
                coroutineScope = viewModelScope
            )
        }
    ).flow
        .map { pagingData ->
            pagingData.map { article ->
                articleUiMapper.toUiModel(article, _bookMarkedArticles.value)
            }
        }
        .cachedIn(viewModelScope)

    init {
        loadBookmarks()
    }

    private fun loadBookmarks() {
        bookMarkedArticleUseCase.invoke(viewModelScope, null) { resource ->
            when (resource) {
                is Resource.Success -> _bookMarkedArticles.value = resource.model
                is Resource.Failure -> {/* handle error */}
                else -> {}
            }
        }
    }

    fun toggleBookmark(article: ArticleUiModel) {
        if (article.isBookmarked) {
            deleteUseCase.invoke(viewModelScope, article.article.id) { resource ->
                when (resource) {
                    is Resource.Success -> uiManager.sendMessage("Deleted")
                    is Resource.Failure -> uiManager.sendMessage(resource.exception.message ?: "Error")
                    else -> {}
                }
            }
        } else {
            saveUseCase.invoke(viewModelScope, article.article.copy(isBookMarked = true)) { resource ->
                when (resource) {
                    is Resource.Success -> uiManager.sendMessage("Saved")
                    is Resource.Failure -> uiManager.sendMessage(resource.exception.message ?: "Error")
                    else -> {}
                }
            }
        }
    }
}
```

---

### 7. UI Layer - Use Paging Normally

```kotlin
// app/src/main/java/com/nassef/weatherapp/screens/pagedMainScreen/PagedMainScreen.kt
@Composable
fun PagedMainScreen(
    navController: NavHostController,
    viewModel: PagingMainScreenViewModel = hiltViewModel()
) {
    val articles = viewModel.articlesFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        // Handle refresh loading
        when (articles.loadState.refresh) {
            is LoadState.Loading -> {
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                val error = (articles.loadState.refresh as LoadState.Error).error
                item {
                    ErrorView(error.message ?: "Unknown error")
                }
            }
            else -> {
                // ✅ Proper LazyPagingItems usage
                items(
                    count = articles.itemCount,
                    key = { index -> articles[index]?.url ?: index }
                ) { index ->
                    articles[index]?.let { article ->
                        ArticleRow(
                            modifier = Modifier,
                            article = article,
                            onArticleClick = {
                                val encodedUrl = URLEncoder.encode(article.url, "UTF-8")
                                navController.navigate("details/${article.id}/$encodedUrl")
                            },
                            onBookMarkClick = {
                                viewModel.toggleBookmark(article)
                            }
                        )
                    }
                }

                // Append loading indicator
                when (articles.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            RetryButton { articles.retry() }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
```

---

## Benefits of This Approach

| Aspect | Benefit |
|--------|---------|
| **Domain Purity** | ✅ No Paging 3 dependency - framework independent |
| **Use Case Pattern** | ✅ Consistent with your architecture |
| **BaseUseCase** | ✅ Loading/Error handling works! |
| **Testability** | ✅ Can test use case without Android framework |
| **Flexibility** | ✅ Can change from Paging 3 to another paging lib easily |
| **Clean Code** | ✅ Each layer has clear responsibility |
| **Paging 3 Benefits** | ✅ Still get all Paging 3 UI optimizations |

---

## Dependency Structure

```kotlin
// domain/build.gradle.kts
dependencies {
    testImplementation(libs.junit)
    implementation(libs.kotlinx.coroutines.android)
    implementation(project(":core"))
    // ✅ NO PAGING DEPENDENCIES!
}

// app/build.gradle.kts
dependencies {
    // ... other deps
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    // ✅ Paging only in presentation layer
}
```

---

## Comparison: Your Approach vs This Approach

| Aspect | Your Approach | This Approach |
|--------|---------------|---------------|
| Domain Dependency | ❌ Has Paging 3 | ✅ Pure Kotlin |
| Use Case | ❌ Can't extend BaseUseCase | ✅ Extends BaseUseCase |
| Loading/Error | ❌ Handle in Compose | ✅ Handle in BaseUseCase |
| Consistency | ❌ Different from other features | ✅ Consistent pattern |
| Testability | ❌ Needs Android framework | ✅ Pure unit tests |
| Paging Benefits | ✅ Yes | ✅ Yes |

---

## Migration Steps

1. Remove Paging 3 from `domain/build.gradle.kts`
2. Create `PaginatedResult` and `PaginationRequest` in domain
3. Add `getArticlesPage()` to repository interface
4. Create `GetPaginatedArticlesUC` extending BaseUseCase
5. Implement repository method in data layer
6. Create `ArticlesPagingSource` in app layer using use case
7. Update ViewModel to build Pager with custom source
8. Update UI to properly use LazyPagingItems

---

## The Best of Both Worlds

✅ Clean Architecture maintained
✅ Use case pattern consistent
✅ BaseUseCase loading/error handling
✅ Paging 3 UI optimizations
✅ Domain stays testable and pure

---

**This is the professional way to implement Paging 3 with Clean Architecture!**
