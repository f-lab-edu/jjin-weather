package com.jin.jjinweather.layer.domain.model.weather

data class DailyWeather(
    val forecastDay: Long,
    val iconResId: Int,
    val minTemperature: Double,
    val maxTemperature: Double
)
