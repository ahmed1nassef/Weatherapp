package com.nassef.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hwasfy.localize.api.LanguageManager
import com.hwasfy.localize.api.currentAppLocale
import com.hwasfy.localize.util.SupportedLocales
import com.nassef.weatherapp.ui.theme.WeatherAppTheme
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var uiManager: UiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentLocale = currentAppLocale()
            WeatherAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    ArticleAppMainScreen( modifier = Modifier , uiManager = uiManager) {
                        var newLocal: SupportedLocales?
                        if (currentLocale == SupportedLocales.EN_US) {
                            newLocal = SupportedLocales.AR_EG
                        } else {
                            newLocal = SupportedLocales.EN_US
                        }
                        LanguageManager.setLanguage(
                            this,
                            newLocal
                        )
                    }
//                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}