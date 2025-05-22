package com.jin.jjinweather.feature.outfit.domain

import com.jin.jjinweather.feature.weather.domain.model.TemperatureSnapshot
import com.jin.jjinweather.feature.weather.domain.model.WeatherIcon
import java.time.Instant

data class HourlyForecastGraph(
    val hourlyTimeStamp: String,
    val temperature: Int
)

fun TemperatureSnapshot.toHourlyForecastGraph(): HourlyForecastGraph = HourlyForecastGraph(
    hourlyTimeStamp = timeStamp.toString(),
    temperature = temperature.toInt()
)

fun HourlyForecastGraph.toHourlyForecast(): TemperatureSnapshot = TemperatureSnapshot(
    timeStamp = Instant.parse(hourlyTimeStamp),
    icon = WeatherIcon.CLEAR_SKY_DAY,
    temperature = temperature
)
