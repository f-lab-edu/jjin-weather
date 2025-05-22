package com.jin.jjinweather.feature.outfitrecommend.ui.recommendcontent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jin.jjinweather.R
import com.jin.jjinweather.feature.weather.domain.model.HourlyForecast
import com.jin.jjinweather.feature.weather.domain.model.TemperatureSnapshot
import com.jin.jjinweather.feature.weather.domain.model.WeatherIcon
import com.jin.jjinweather.ui.theme.JJinWeatherTheme
import java.time.Instant
import java.time.ZoneId

@Composable
fun OutfitRecommendScreen(
    imageUrl: String?,
    cityName: String,
    summary: String,
    forecast: HourlyForecast,
    feelsLikeTemperature: Int,
    onNavigateToTemperature: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            OutfitHeader(onNavigateToTemperature)
            CityNameAndWeatherSummary(cityName, summary)
            if (imageUrl != null) {
                OutfitSuccess(imageUrl)
            } else {
                OutfitError()
            }
            Text(
                text = stringResource(R.string.outfit_hourly_forecast_graph),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
            )
            HourlyForecastGraph(
                forecast.map { it.temperature.toInt() },
                forecast.map { it.timeStamp.atZone(ZoneId.systemDefault()).hour },
                feelsLikeTemperature,
                Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
            )
        }
    }
}
