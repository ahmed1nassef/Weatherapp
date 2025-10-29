package com.nassef.weatherapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.nassef.weatherapp.navigation.WeatherDestinations

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,navController : NavHostController
){
    Column {
        Text("MainScreen")
        Button({
            navController.navigate(WeatherDestinations.ARTICLE_MAIN_ROUTE)
        }) {
            Text("home")
        }
    }

}