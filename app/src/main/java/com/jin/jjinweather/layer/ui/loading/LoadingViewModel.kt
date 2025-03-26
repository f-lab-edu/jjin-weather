package com.jin.jjinweather.layer.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.jjinweather.layer.domain.model.UiState
import com.jin.jjinweather.layer.domain.model.weather.Weather
import com.jin.jjinweather.layer.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadingViewModel(private val getWeatherUseCase: GetWeatherUseCase) : ViewModel() {
    private val _weatherState = MutableStateFlow<UiState<Weather>>(UiState.Loading)
    val weatherState: StateFlow<UiState<Weather>> = _weatherState

    init {
        loadWeather()
    }

    private fun loadWeather() {
        viewModelScope.launch {
            try {
                val weather = getWeatherUseCase()
                _weatherState.value = UiState.Success(weather)
            } catch (e: Exception) {
                _weatherState.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }


    // todo : 앱 첫 실행 여부 판단
    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

}
