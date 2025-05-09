package com.jin.jjinweather.feature.weather.data.model.dto

import com.google.gson.annotations.SerializedName

data class DailyWeatherDTO(
    @SerializedName("dt") val dt: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("moon_phase") val moonPhase: Double,
    @SerializedName("temp") val temperature: TemperatureDTO,
    @SerializedName("weather") val weather: List<WeatherConditionDTO>
)
