package com.nassef.weatherapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nassef.domain.entities.Article
import com.nassef.weatherapp.R

@Composable
fun ArticleRow(
    modifier: Modifier,
    article: Article,
    onArticleClick: () -> Unit,
    onBookMarkClick: () -> Unit
) {
    val articleBookMarkIcon = if (article.isBookMarked) Icons.Default.AddTask else Icons.Default.Add
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable(onClick = {
                onArticleClick()
            }),
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
                    Text(modifier = modifier.padding(5.dp), text = article.source.name, color = Color.Gray)
                    Text(modifier = modifier.padding(5.dp), text = "• ", color = Color.Gray)
                    article.publishedAt?.let {
                        Text(
                            modifier = modifier.padding(5.dp),
                            text = it,
                            color = Color.Gray
                        )
                    }
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
//                        viewModel.addArticleToBookMarks(article)
                        onBookMarkClick()
                    }) {
                        Icon(imageVector = articleBookMarkIcon, contentDescription = "add bookmark")
                    }

                }
            }

        }
    }
}
