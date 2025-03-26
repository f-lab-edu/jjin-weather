package com.jin.jjinweather.layer.domain.repository

import com.jin.jjinweather.layer.domain.model.weather.Weather

interface WeatherRepository {
    suspend fun loadWeather(): Weather
}
