package com.nassef.weatherapp.screens.landingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassef.core.data.model.Resource
import com.nassef.domain.features.splashHistory.interactor.GetStartDestinationUC
import com.nassef.weatherapp.utils.UiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LandingScreenViewModel @Inject constructor(val landingUC: GetStartDestinationUC, val uiManager: UiManager) : ViewModel() {
    val _isOpened = MutableStateFlow(false)
    val isOpened = _isOpened.asStateFlow()

    init {
        landingUC.invoke(scope = viewModelScope, body = true) {
            when (it) {
                is Resource.Failure -> it.exception.message
                is Resource.Progress<*> -> it.loading
                is Resource.Success -> {
                    _isOpened.value = it.model
//                    if(_isOpened.value)
//                        sendMsg("true")
//                    else
//                        sendMsg("false")
                }
            }
        }
    }

    fun sendMsg( msg : String){
        uiManager.sendMessage(msg)
    }
}