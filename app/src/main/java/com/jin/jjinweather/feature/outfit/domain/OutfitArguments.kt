package com.jin.jjinweather.feature.outfit.domain

import com.jin.jjinweather.feature.weather.domain.model.HourlyForecast

data class OutfitArguments(
    val temperature: Int,
    val cityName: String,
    val weatherSummary: String,
    val hourlyForecast: HourlyForecast,
    val feelsLikeTemperature: Int
)
