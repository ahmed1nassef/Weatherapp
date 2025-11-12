package com.nassef.weatherapp.screens.articleDeatilsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.nassef.weatherapp.R

@Preview
@Composable
fun ArticleDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: ArticleDetailsViewModel = hiltViewModel(),
    articleId: Int = -1,
    articleUrl: String? = ""
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.currentArticle != null) {
        Box(modifier = modifier.fillMaxWidth()) {
            ArticleDetails(modifier, uiState, navController)
            ElevatedButton(
                modifier = modifier.padding(10.dp),
                onClick = {
                    navController.popBackStack()
                },
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "back")
            }
        }
    } /*else if (uiState.article.isNotEmpty())
        viewModel.getBookMarkedArticle(articleId)*/
    else if (uiState.error != null) {
        viewModel.sendMsg(uiState.error!!)
    }
}

@Composable
private fun ArticleDetails(
    modifier: Modifier,
    uiState: DetailsScreenUi,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Column(modifier = modifier.fillMaxSize()) {
            AsyncImage(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentDescription = "image",
                model = uiState.currentArticle?.urlToImage,
                placeholder = painterResource(R.drawable.placeholder_image),
//                                error = painterResource(R.drawable.error_image),
                error = painterResource(R.drawable.placeholder_image),
                contentScale = ContentScale.FillBounds,
                onError = {
                    print(it.result)
                }
            )
            Text(
                modifier = modifier.padding(10.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                text = uiState.currentArticle!!.title
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    modifier = modifier.padding(10.dp),
                    fontSize = 15.sp,
                    text = uiState.currentArticle!!.source.name,
                    color = Color.Gray
                )
                uiState.currentArticle!!.publishedAt?.let {
                    Text(
                        modifier = modifier.padding(10.dp),
                        fontSize = 15.sp,
                        //                        text = "Oct 20, 2025 • 5 min read",
                        text = it,
                        color = Color.Gray
                    )
                }
            }
            HorizontalDivider(modifier = modifier.padding(10.dp))
            uiState.currentArticle!!.content?.let {
                Text(
                    modifier = modifier.padding(10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.Serif,
                    text = it
                )
            }


        }
    }
}