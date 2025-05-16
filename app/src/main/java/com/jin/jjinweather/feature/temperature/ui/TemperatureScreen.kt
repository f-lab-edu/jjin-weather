package com.jin.jjinweather.feature.temperature.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.jin.jjinweather.feature.temperature.ui.weathercontent.WeatherErrorScreen
import com.jin.jjinweather.feature.temperature.ui.weathercontent.WeatherLoadingScreen
import com.jin.jjinweather.feature.temperature.ui.weathercontent.success.WeatherSuccessScreen
import com.jin.jjinweather.feature.weather.domain.model.CityWeather
import com.jin.jjinweather.feature.weather.ui.state.UiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TemperatureScreen(viewModel: TemperatureViewModel, onNavigateToOutfit: (Int) -> Unit) {
    val composePermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val weather by viewModel.weatherState.collectAsState()

    LaunchedEffect(composePermissionState.status) {
        if (composePermissionState.status is PermissionStatus.Granted) {
            viewModel.onLocationPermissionGranted()
        }
    }
    // FIXME : 불필요한 함수 분리로 판단되어 OutfitScreen UI 수정 시 수정 예정
    WeatherContentUI(weather, onNavigateToOutfit)
}

@Composable
private fun WeatherContentUI(weather: UiState<CityWeather>, onNavigateToOutfit: (Int) -> Unit) {
    when (weather) {
        is UiState.Loading -> WeatherLoadingScreen()
        is UiState.Success -> WeatherSuccessScreen(weather.data, onNavigateToOutfit)
        is UiState.Error -> WeatherErrorScreen(weather.message)
    }
}
