package com.nassef.weatherapp.screens.mainScreen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.nassef.domain.entities.ArticleX
import com.nassef.domain.utilities.categoriesList
import com.nassef.weatherapp.R


@Preview(
    name = "main screen",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val searchText = rememberSaveable {
        mutableStateOf("")
    }

    val uiState by viewModel.uiState.collectAsState()


    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                modifier = modifier.fillMaxWidth(),
                color = colorResource(R.color.light_gray)
            ) {
                ArticleSearchSection(modifier = modifier, searchText.value) {
                    searchText.value = it
                    viewModel.searchArticle(it)
                }
            }

            HorizontalDivider(modifier = modifier)

            LazyRow(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically/*,
                horizontalArrangement = Arrangement.SpaceEvenly*/
            ) {
                items(categoriesList) {
                    CategoriesRow(modifier, it, uiState.category == it){
                        viewModel.searchCategory(it)
                    }
                }
            }

            if (uiState.isLoading) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.articles.isEmpty().not()) {
                ArtcilesSection(modifier, uiState, viewModel) {
                    searchText.value = ""
                    viewModel.refreshArticles()
                }
            } else if (uiState.error.isNullOrEmpty().not()) {
                viewModel.sendMessage(uiState.error!!)
            }
        }

    }


}

@Composable
fun ArticleSearchSection(modifier: Modifier, searchText: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        onValueChange = { txt ->
            onValueChanged(txt)
        },
        value = searchText,
        label = {
            Text("search")
        },
        placeholder = {
            Text("search article")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
private fun ArtcilesSection(
    modifier: Modifier,
    uiState: UiState,
    viewModel: MainScreenViewModel,
    onRefreshArticles: () -> Unit
) {
    val refreshState = rememberPullToRefreshState()
    val animatedRefreshState = remember {
        object : PullToRefreshState {
            private val anim = Animatable(0f, Float.VectorConverter)
            override val distanceFraction
                get() = anim.value
            override val isAnimating: Boolean
                get() = uiState.isRefreshing

            override suspend fun animateToThreshold() {
                anim.animateTo(1f, spring(dampingRatio = Spring.DampingRatioHighBouncy))
            }

            override suspend fun animateToHidden() {
                anim.animateTo(0f)
            }

            override suspend fun snapTo(targetValue: Float) {
                anim.snapTo(targetValue)
            }
        }
    }

    HorizontalDivider(modifier = modifier.padding(bottom = 5.dp))
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = {
//            viewModel.refreshArticles()
            onRefreshArticles
        },
        modifier = modifier,
        state = refreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.isRefreshing,
                state = refreshState
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
//                verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(uiState.articles) { article ->
                ArticleRow(modifier, article, viewModel)
            }
        }
    }


}


@Composable
private fun ArticleRow(modifier: Modifier, article: ArticleX, viewModel: MainScreenViewModel) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentDescription = "image",
//                                clipToBounds = true,
                model = article.urlToImage,
                placeholder = painterResource(R.drawable.placeholder_image),
//                                error = painterResource(R.drawable.error_image),
                error = painterResource(R.drawable.placeholder_image),
                contentScale = ContentScale.FillBounds,
                onError = {
                    print(it.result)
                }
            )
            Column(modifier = modifier.padding(5.dp)) {
                Row(modifier = modifier.fillMaxWidth()) {
                    Text(modifier = modifier.padding(5.dp), text = "TechCrunch", color = Color.Gray)
                    Text(modifier = modifier.padding(5.dp), text = "• ", color = Color.Gray)
                    Text(
                        modifier = modifier.padding(5.dp),
                        text = article.publishedAt,
                        color = Color.Gray
                    )
                }
                Text(
                    modifier = modifier.padding(5.dp),
                    text = article.title,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.StartEllipsis
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = modifier.padding(5.dp), text = "5 min read")
                    IconButton(onClick = {
                        viewModel.addArticleToBookMarks(article)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add bookmark")
                    }

                }
            }

        }
    }
}

@Composable
private fun CategoriesRow(modifier: Modifier, string: String, isSelected: Boolean , onCategoryClick : ()-> Unit) {
    val color: Color =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    Surface(
        modifier = modifier.padding(5.dp).clickable(onClick = {
            onCategoryClick()
        }),
        shape = RoundedCornerShape(30.dp),
        color = color
    ) {
        Text(
            modifier = modifier.padding(10.dp), text = string,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}