package com.jin.jjinweather.feature.outfitrecommend.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.jin.jjinweather.feature.temperature.ui.weathercontent.WeatherErrorScreen
import com.jin.jjinweather.feature.temperature.ui.weathercontent.WeatherLoadingScreen
import com.jin.jjinweather.feature.weather.ui.state.UiState

@Composable
fun OutfitScreen(viewModel: OutfitViewModel, temperature: Int) {
    val outfitImageUrl by viewModel.outfitImageUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOutfitImageForTemperature(temperature)
    }

    // FIXME : Loading, error 화면 수정 필요.
    when (val state = outfitImageUrl) {
        is UiState.Loading -> WeatherLoadingScreen()
        is UiState.Success ->
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    model = state.data, contentDescription = ""
                )
            }

        is UiState.Error -> WeatherErrorScreen("outfitImageUrl")
    }

}
