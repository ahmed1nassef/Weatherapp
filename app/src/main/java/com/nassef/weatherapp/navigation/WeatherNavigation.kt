package com.nassef.weatherapp.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nassef.weatherapp.screens.ArticleDetailsScreen
import com.nassef.weatherapp.screens.BookMarksScreen
import com.nassef.weatherapp.screens.mainScreen.MainScreen
import com.nassef.weatherapp.screens.SettingsScreen
import com.nassef.weatherapp.screens.SplashScreen
import com.nassef.weatherapp.screens.mainScreen.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun WeatherNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentRout : String,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = WeatherDestinations.SPLASH_SCREEN_ROUTE,
    navAction: WeatherNavigationActions = remember(navController) {
        WeatherNavigationActions(navController)
    }
) {

//    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRout = currentNavBackStackEntry?.destination?.route ?: startDestination



    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = WeatherDestinations.ARTICLE_MAIN_ROUTE){
            MainScreen(navController = navController)
        }
        composable(route = WeatherDestinations.ARTICLE_DETAILS_ROUTE){
            ArticleDetailsScreen()
        }
        composable(route = WeatherDestinations.SETTINGS_SCREEN_ROUTE){
            SettingsScreen()
        }
        composable(route = WeatherDestinations.BOOK_MARKS_SCREEN_ROUTE){
            BookMarksScreen()
        }
        composable(route = WeatherDestinations.SPLASH_SCREEN_ROUTE){
            SplashScreen(navController = navController)
        }
    }


}