package com.nassef.weatherapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nassef.weatherapp.navigation.Destination
import com.nassef.weatherapp.navigation.WeatherBottomNav
import com.nassef.weatherapp.navigation.WeatherDestinations
import com.nassef.weatherapp.navigation.WeatherNavGraph
import com.nassef.weatherapp.navigation.WeatherTopBar
import com.nassef.weatherapp.utils.UiManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleAppMainScreen(modifier: Modifier = Modifier, uiManager: UiManager) {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableIntStateOf(-1) }

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRout =
        currentNavBackStackEntry?.destination?.route ?: WeatherDestinations.SPLASH_SCREEN_ROUTE

    val isShowBottomNav by rememberSaveable(currentRout) {
        mutableStateOf(Destination.entries.find {
            it.route == currentRout
        } != null)
    }
    val isShowTopBar by rememberSaveable(currentRout) {
        mutableStateOf(Destination.entries.find {
            it.route == currentRout
        } != null)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    LaunchedEffect(snackbarHostState) {
        coroutineScope.launch {
            uiManager.snackbarMessage.collect {
                snackbarHostState.showSnackbar(it , duration = SnackbarDuration.Long)
            }
        }
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            WeatherBottomNav(
                isShowBottomNav = isShowBottomNav,
                selectedDestination = selectedDestination,
                navController = navController
            ) { index ->
                selectedDestination = index
            }

        },
        topBar = {
            WeatherTopBar(
                isShowTopBar = isShowTopBar,
                selectedDestination = selectedDestination,
                navController = navController
            ) {

            }
        }
    ) { contentPadding ->
        WeatherNavGraph(
            modifier = modifier.padding(contentPadding),
            navController = navController,
            currentRout = currentRout,
            coroutineScope = coroutineScope
        )
    }
}