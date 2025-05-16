package com.jin.jjinweather.feature.weather.data.model.dto

import com.google.gson.annotations.SerializedName

data class FeelsLikeTemperatureRangeDto(
    @SerializedName("day") val day: Double,
    @SerializedName("night") val night: Double
)
