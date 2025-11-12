package com.nassef.weatherapp.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nassef.weatherapp.R
import com.nassef.weatherapp.utils.UiManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class Destination(
    val route: String,
    val label: Int,
    val icon: ImageVector,
    val contentDescription: String
) {
    MainScreen(
        route = WeatherDestinations.ARTICLE_MAIN_ROUTE,
        label = R.string.main_screen_lable,
        icon = Icons.Default.Home,
        contentDescription = "main page"
    ),
    BookMarksScreen(
        route = WeatherDestinations.BOOK_MARKS_SCREEN_ROUTE,
        label = R.string.bookmarks_screen_lable,
        icon = Icons.Default.AddCircle,
        contentDescription = "bookmarks screen"
    ),
    SettingsScreen(
        route = WeatherDestinations.SETTINGS_SCREEN_ROUTE,
        label = R.string.settings_screen_lable,
        icon = Icons.Default.Settings,
        contentDescription = "settings screen"
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopBar(
    modifier: Modifier = Modifier,
    isShowTopBar: Boolean,
    selectedDestination: Int,
    navController: NavHostController,
    onOpenDrawer: () -> Unit
){
    if(isShowTopBar){
        TopAppBar(title = {
            Text("Weather app")
        } , modifier = modifier , navigationIcon = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above
                    ),
                tooltip = { PlainTooltip { Text("Menu") } },
                state = rememberTooltipState(),
            ) {
                IconButton(onClick = { onOpenDrawer() }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        },
            actions = {
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above
                        ),
                    tooltip = { PlainTooltip { Text("Add to favorites") } },
                    state = rememberTooltipState(),
                ) {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Add to favorites",
                        )
                    }
                }
            })
    }
}
@Composable
fun WeatherBottomNav(
    isShowBottomNav: Boolean,
    selectedDestination: Int,
    navController: NavHostController,
    onSelectedChange: (Int) -> Unit
) {
    if (isShowBottomNav) {
        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {

            Destination.entries.forEachIndexed { index, destination ->
                NavigationBarItem(
                    selected = selectedDestination == index,
                    onClick = {
                        navAndPopUpTo(
                            navHostController = navController,
                            route = destination.route,
                            clearBackStack = true,
                            popUpRoute = WeatherDestinations.ARTICLE_MAIN_ROUTE
                        )
//                        navController.navigate(route = destination.route)
                        onSelectedChange(index)
//                        selectedDestination = index
                    },
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.contentDescription
                        )
                    },
                    label = { Text(stringResource(destination.label)) }
                )

            }
        }
    }
}
