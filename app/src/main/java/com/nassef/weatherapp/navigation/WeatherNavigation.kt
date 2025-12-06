package com.nassef.weatherapp.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nassef.weatherapp.navigation.WeatherDistArgs.ARTICLE_ID_NAV_ARG
import com.nassef.weatherapp.navigation.WeatherDistArgs.ARTICLE_URL_NAV_ARG
import com.nassef.weatherapp.screens.articleDeatilsScreen.ArticleDetailsScreen
import com.nassef.weatherapp.screens.bookMarksScreen.BookMarksScreen
//import com.nassef.weatherapp.screens.mainScreen.MainScreen
import com.nassef.weatherapp.screens.SettingsScreen
import com.nassef.weatherapp.screens.landingScreen.SplashScreen
import com.nassef.weatherapp.screens.pagedMainScreen.MainScreen
import kotlinx.coroutines.CoroutineScope
import java.net.URLDecoder

@Composable
fun WeatherNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    backStackEntry: NavBackStackEntry?,
    currentRout : String,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = WeatherDestinations.SPLASH_SCREEN_ROUTE/*,
    navAction: WeatherNavigationActions = remember(navController) {
        WeatherNavigationActions(navController)
    }*/
) {

//    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRout = currentNavBackStackEntry?.destination?.route ?: startDestination


//    arguments = listOf(navArgument("type") { type = NavType.StringType })
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = WeatherDestinations.ARTICLE_MAIN_ROUTE){
            MainScreen(navController = navController)
        }
        composable(route = WeatherDestinations.ARTICLE_DETAILS_ROUTE , arguments = listOf(
            navArgument(ARTICLE_ID_NAV_ARG) { type = NavType.IntType },
            navArgument(ARTICLE_URL_NAV_ARG) { type = NavType.StringType }
        )){ entry ->
            val articleId = entry.arguments?.getInt(ARTICLE_ID_NAV_ARG) ?: -1
            val articleUrl = entry.arguments?.getString(ARTICLE_URL_NAV_ARG , "").apply {
                URLDecoder.decode(this, "UTF-8")
            }
            ArticleDetailsScreen(navController = navController, articleId = articleId, articleUrl = articleUrl)
        }
        composable(route = WeatherDestinations.SETTINGS_SCREEN_ROUTE){
            SettingsScreen()
        }
        composable(route = WeatherDestinations.BOOK_MARKS_SCREEN_ROUTE){
            BookMarksScreen(navController = navController)
        }
        composable(route = WeatherDestinations.SPLASH_SCREEN_ROUTE){
            SplashScreen(navController = navController)
        }
    }


}

fun navAndPopUpTo(
    navHostController: NavHostController,
    route: String,
    clearBackStack: Boolean = false,
    popUpRoute :String = WeatherDestinations.SPLASH_SCREEN_ROUTE
) {
    navHostController.navigate(route) {
        popUpTo(popUpRoute) {
            inclusive = clearBackStack
        }
    }
}