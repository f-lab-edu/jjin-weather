package com.jin.jjinweather.layer.domain.model.weather

data class Weather(
    val cityName: String,
    val iconResId: Int,
    val currentTemperature: Int,
    val yesterdayTemperature: Int,
    val minTemperature: Int,
    val maxTemperature: Int,
    val hourlyWeatherList: List<HourlyWeather>,
    val dailyWeatherList: List<DailyWeather>,
    val sunrise: Long,
    val sunset: Long,
    val moonPhase: Double
)
