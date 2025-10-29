package com.nassef.weatherapp.navigation

import androidx.navigation.NavHostController
import com.nassef.weatherapp.navigation.WeatherScreens.ARTICLE_DETAILS_SCREEN
import com.nassef.weatherapp.navigation.WeatherScreens.ARTICLE_MAIN_SCREEN
import com.nassef.weatherapp.navigation.WeatherScreens.BOOK_MARKS_SCREEN
import com.nassef.weatherapp.navigation.WeatherScreens.SETTINGS_SCREEN
import com.nassef.weatherapp.navigation.WeatherScreens.SPLASH_SCREEN


object WeatherScreens {
    const val ARTICLE_MAIN_SCREEN = "article_lists"
    const val ARTICLE_DETAILS_SCREEN = "article_details"
    const val BOOK_MARKS_SCREEN = "bookmarks"
    const val SETTINGS_SCREEN = "settings"
    const val SPLASH_SCREEN = "splash"
}

object WeatherDistArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val ARTICLE_ID_ARG = "taskId"
    const val TITLE_ARG = "title"
}

object WeatherDestinations {
    const val ARTICLE_MAIN_ROUTE = "$ARTICLE_MAIN_SCREEN"
    const val ARTICLE_DETAILS_ROUTE = "$ARTICLE_DETAILS_SCREEN"
    const val SETTINGS_SCREEN_ROUTE = "$SETTINGS_SCREEN"
    const val BOOK_MARKS_SCREEN_ROUTE = "$BOOK_MARKS_SCREEN"

    const val SPLASH_SCREEN_ROUTE = "$SPLASH_SCREEN"
}

class WeatherNavigationActions(private val navController : NavHostController){

}

