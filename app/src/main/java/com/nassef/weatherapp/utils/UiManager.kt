package com.nassef.weatherapp.utils

import com.nassef.weatherapp.di.MainDispatcher
import com.nassef.weatherapp.di.MainImmediateDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiManager @Inject constructor(@MainImmediateDispatcher private val dispatcher: CoroutineDispatcher){
    private val _snackbarMessage = Channel<String>()
    val snackbarMessage = _snackbarMessage.receiveAsFlow()

    fun sendMessage(msg : String){
        GlobalScope.launch(dispatcher) {
            _snackbarMessage.send(msg)
        }
    }
}