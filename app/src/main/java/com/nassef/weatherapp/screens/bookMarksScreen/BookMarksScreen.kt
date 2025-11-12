package com.nassef.weatherapp.screens.bookMarksScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nassef.weatherapp.R
import com.nassef.weatherapp.components.ArticleRow
import com.nassef.weatherapp.navigation.WeatherScreens

@Composable
fun BookMarksScreen(
    modifier: Modifier = Modifier,
    viewModel: BookMarksViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isArticleDeleted)
        viewModel.sendMsg("deleted successfully")
    Surface(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = modifier)

            }
        } else if (uiState.articles.isNotEmpty()) {
            LazyColumn {
                items(uiState.articles) { article ->
                    ArticleRow(modifier, article, onArticleClick = {
                        val detailsScreen = WeatherScreens.ARTICLE_DETAILS_SCREEN
                        val encodedUrl = java.net.URLEncoder.encode(article.url, "UTF-8")
                        navController.navigate("$detailsScreen/${article.id}/${encodedUrl}")
                    }) {
                        viewModel.deleteArticleById(article.id)
                    }
                }
            }
        } else {
            NoBookMarksView(modifier)
        }

    }

}

@Preview("no bookmarks")
@Composable
private fun NoBookMarksView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.article_icon),
            contentDescription = "no articles",
            modifier = modifier.size(60.dp)
        )
        Text(
            text = "No bookmarks yet",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Save articles to read them later",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}