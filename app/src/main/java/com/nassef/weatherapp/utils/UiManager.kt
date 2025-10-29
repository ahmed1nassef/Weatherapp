package com.nassef.weatherapp.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiManager @Inject constructor(){
    private val _snackbarMessage = Channel<String>()
    val snackbarMessage = _snackbarMessage.receiveAsFlow()

    fun sendMessage(msg : String){
        GlobalScope.launch(Dispatchers.Main.immediate) {
            _snackbarMessage.send(msg)
        }
    }
}