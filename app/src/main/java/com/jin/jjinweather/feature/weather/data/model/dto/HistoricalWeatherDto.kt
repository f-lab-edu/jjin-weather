package com.jin.jjinweather.feature.weather.data.model.dto

import com.google.gson.annotations.SerializedName

data class HistoricalWeatherDto(
    @SerializedName("data") val data: List<HistoricalTempDto>
)

data class HistoricalTempDto(
    @SerializedName("temp") val temperature: Double
)
