package com.nassef.weatherapp

import android.app.Application
import android.content.Context
import com.hwasfy.localize.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArticlesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        super.attachBaseContext(LocaleHelper.wrapContext(base!!))
        base?.let { super.attachBaseContext(LocaleHelper.wrapContext(it)) } ?: super.attachBaseContext(base)
    }
}