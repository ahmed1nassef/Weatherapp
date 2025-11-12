package com.nassef.weatherapp.screens.landingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nassef.weatherapp.navigation.WeatherDestinations
import com.nassef.weatherapp.navigation.navAndPopUpTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Preview
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
//    viewmodel: LandingScreenViewModel = hiltViewModel()
) {
//    val isOpened by viewmodel.isOpened.collectAsState()
//    if(isOpened.value)
//        viewmodel.sendMsg("opened")

//    if (isOpened) {
//        navAndPopUpTo(navController , WeatherDestinations.ARTICLE_MAIN_ROUTE , true)
    /*navController.navigate(
        route = WeatherDestinations.ARTICLE_MAIN_ROUTE
    ) {
        popUpTo(WeatherDestinations.SPLASH_SCREEN_ROUTE) {
            inclusive = true
        }
    }*/
//    }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState {
        4
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        SplashPager(pagerState, modifier, scope, navController, maxHeight)

        SplashIndicators(pagerState)
    }
    Box(modifier = modifier.fillMaxSize()) {


    }

}

@Composable
private fun SplashPager(
    pagerState: PagerState,
    modifier: Modifier,
    scope: CoroutineScope,
    navController: NavHostController,
    maxHeight: Dp
) {
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val pagerOffset = pagerState.getOffsetDistanceInPages(page)

        Card(
            modifier
                .padding(10.dp)
                .height((maxHeight / 2) * (1 - pagerOffset))
//            modifier.padding(10.dp).height(400.dp * (1 - pagerOffset))
                .graphicsLayer {
                    //this block of code is for Apply item scroll effects to content

                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }, elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            if (page == 0)
                FirstPage(scope = scope, pagerState = pagerState)
            else if (page == 1)
                SecondPage()
            else if (page == 2)
                SecondPage()
            else
                FinalPage(navController = navController)
        }

    }
}

@Composable
private fun BoxScope.SplashIndicators(pagerState: PagerState) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}

@Composable
fun FirstPage(modifier: Modifier = Modifier, scope: CoroutineScope, pagerState: PagerState) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = Icons.Default.ContactPage,
            contentDescription = "i",
            modifier = modifier.align(
                Alignment.TopCenter
            )
        )
        Button(
            modifier = modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(3)
                }
            }) {
            Text("skip")
        }


    }

}

@Composable
fun SecondPage(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(imageVector = Icons.Default.ContactPage, contentDescription = "i")

    }

}

@Composable
fun FinalPage(modifier: Modifier = Modifier, navController: NavHostController) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = Icons.Default.ContactPage,
            contentDescription = "i",
            modifier = modifier.align(
                Alignment.TopCenter
            )
        )
        Button(
            modifier = modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                navAndPopUpTo(navController, WeatherDestinations.ARTICLE_MAIN_ROUTE, true)
                /* navController.navigate(
                     route = WeatherDestinations.ARTICLE_MAIN_ROUTE
                 ) {
                     popUpTo(WeatherDestinations.SPLASH_SCREEN_ROUTE) {
                         inclusive = true
                     }
                 }*/
            }) {
            Text("continue")
        }


    }

}