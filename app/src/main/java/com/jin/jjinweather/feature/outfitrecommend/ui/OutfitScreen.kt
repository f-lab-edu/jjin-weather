package com.jin.jjinweather.feature.outfitrecommend.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jin.jjinweather.feature.outfitrecommend.ui.recommendcontent.OutfitRecommendScreen
import com.jin.jjinweather.feature.temperature.ui.weathercontent.WeatherLoadingScreen
import com.jin.jjinweather.feature.weather.domain.model.HourlyForecast
import com.jin.jjinweather.feature.weather.ui.state.UiState

@Composable
fun OutfitScreen(
    viewModel: OutfitViewModel,
    temperature: Int,
    cityName: String,
    summary: String,
    forecast: HourlyForecast,
    feelsLikeTemperature: Int,
    onNavigateToTemperature: () -> Unit
) {
    val outfitImageUrl by viewModel.outfitImageUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOutfitImageForTemperature(temperature)
    }

    when (val state = outfitImageUrl) {
        is UiState.Loading -> OutfitLoadingScreen()
        is UiState.Success -> OutfitRecommendScreen(
            state.data,
            cityName,
            summary,
            forecast,
            feelsLikeTemperature,
            onNavigateToTemperature
        )

        else -> OutfitRecommendScreen(
            null,
            cityName,
            summary,
            forecast,
            feelsLikeTemperature,
            onNavigateToTemperature
        )
    }
}
