package com.jin.jjinweather.layer.ui.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.jin.jjinweather.layer.domain.model.UiState
import com.jin.jjinweather.layer.domain.model.weather.Weather

@Composable
fun LoadingScreen(viewModel: LoadingViewModel, onNavigateToOnboarding: () -> Unit) {
    val weatherState by viewModel.weatherState.collectAsState()

    when (val state = weatherState) {
        is UiState.Loading -> WeatherLoadingContent()
        is UiState.Success -> WeatherLoadedContent(state.data, onNavigateToOnboarding)
        is UiState.Error -> WeatherErrorContent(state.message)
    }
}

@Composable
fun WeatherLoadingContent() {
    // indicator
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Loading...", fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun WeatherLoadedContent(weather: Weather, onNavigateToOnboarding: () -> Unit) {
    // success 한 후, isFirstUser(첫실행 user 는 tutorial(onboarding)로, 아니라면 바로 main)
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            Column {
                Text("위치 : ${weather.cityName}")
                Text("현재 온도 : ${weather.currentTemperature}")
                Button(onClick = onNavigateToOnboarding) { Text("GO TO TUTORIAL") }
            }
        }
    }
}

@Composable
fun WeatherErrorContent(message: String) {
    // 적절한 예외처리 필요
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Error : $message", color = Color.Red)
            }
        }
    }
}
