package com.jin.jjinweather.feature.weather.data.model.dto

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("current") val current: CurrentWeatherDto,
    @SerializedName("hourly") val hourly: List<HourlyWeatherDto>,
    @SerializedName("daily") val daily: List<DailyWeatherDto>
)
