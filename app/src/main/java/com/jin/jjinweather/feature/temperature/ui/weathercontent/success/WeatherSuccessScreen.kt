package com.jin.jjinweather.feature.temperature.ui.weathercontent.success

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jin.jjinweather.feature.weather.domain.model.CityWeather
import com.jin.jjinweather.feature.weather.domain.model.HourlyForecast
import com.jin.jjinweather.ui.theme.SuccessBackgroundBottomDayColor
import com.jin.jjinweather.ui.theme.SuccessBackgroundBottomNightColor
import com.jin.jjinweather.ui.theme.SuccessBackgroundTopDayColor
import com.jin.jjinweather.ui.theme.SuccessBackgroundTopNightColor
import com.jin.jjinweather.ui.theme.SuccessCardBackgroundDayColor
import com.jin.jjinweather.ui.theme.SuccessCardBackgroundNightColor
import java.time.LocalTime

@Composable
fun WeatherSuccessScreen(
    weather: CityWeather,
    pageCount: Int,
    currentPage: Int,
    onNavigateToOutfit: (
        temperature: Int,
        cityName: String,
        summary: String,
        forecast: HourlyForecast,
        feelsLikeTemperature: Int,
    ) -> Unit,
    onNavigateToDistrict: () -> Unit
) {
    val now = LocalTime.now()
    val isNight =
        now.isBefore(weather.weather.dayWeather.sunCycle.sunrise) || now.isAfter(weather.weather.dayWeather.sunCycle.sunset)
    val backgroundGradientBrush = generateBackgroundColor(isNight)
    val cardBackgroundColor = generatedCardBackgroundColor(isNight)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradientBrush)
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { TopMenuAction(onNavigateToDistrict) }
            item {
                CurrentWeatherOverview(
                    pageCount = pageCount,
                    currentPage = currentPage,
                    cityName = weather.cityName,
                    currentTemperature = weather.weather.dayWeather.temperature.toInt(),
                    currentWeatherIconRes = weather.weather.dayWeather.icon.drawableRes,
                    todayMinTemperature = weather.weather.dayWeather.temperatureRange.min.toInt(),
                    todayMaxTemperature = weather.weather.dayWeather.temperatureRange.max.toInt(),
                    yesterdayTemperature = weather.weather.yesterdayWeather.temperature.toInt()
                )
            }
            item {
                YesterdayWeatherOutfit(
                    backgroundColor = cardBackgroundColor,
                    yesterdayTemperature = weather.weather.yesterdayWeather.temperature.toInt(),
                    onNavigateToOutfit = {
                        onNavigateToOutfit(
                            weather.weather.dayWeather.temperature.toInt(),
                            weather.cityName,
                            weather.weather.forecast.daily.firstOrNull()?.summary.orEmpty(),
                            weather.weather.forecast.hourly,
                            weather.weather.dayWeather.feelsLikeTemperature.toInt()
                        )
                    }
                )
            }
            item {
                HourlyForecast(
                    modifier = Modifier.padding(bottom = 10.dp),
                    backgroundColor = cardBackgroundColor,
                    hourlyWeatherList = weather.weather.forecast.hourly
                )
            }
            item {
                DailyForecast(
                    modifier = Modifier.padding(bottom = 10.dp),
                    backgroundColor = cardBackgroundColor,
                    dailyWeatherList = weather.weather.forecast.daily
                )
            }
            item {
                val today = weather.weather.dayWeather.date
                val tomorrow = weather.weather.forecast.daily.find { it.date.after(today) }
                val nextSunrise = tomorrow?.sunCycle?.sunrise ?: weather.weather.dayWeather.sunCycle.sunrise
                DetailWeather(
                    modifier = Modifier.padding(bottom = 20.dp),
                    backgroundColor = cardBackgroundColor,
                    sunrise = weather.weather.dayWeather.sunCycle.sunrise,
                    sunset = weather.weather.dayWeather.sunCycle.sunset,
                    nextSunrise = nextSunrise,
                    moonPhase = weather.weather.dayWeather.moonPhase,
                    weatherDescription = weather.weather.dayWeather.description,
                    feelsLikeTemperature = weather.weather.dayWeather.feelsLikeTemperature.toInt(),
                )
            }
        }
    }
}

private fun generateBackgroundColor(isNight: Boolean): Brush = Brush.verticalGradient(
    colors = if (isNight) {
        listOf(SuccessBackgroundTopNightColor, SuccessBackgroundBottomNightColor)
    } else {
        listOf(SuccessBackgroundTopDayColor, SuccessBackgroundBottomDayColor)
    }
)

private fun generatedCardBackgroundColor(isNight: Boolean): Color =
    if (isNight) SuccessCardBackgroundNightColor else SuccessCardBackgroundDayColor
