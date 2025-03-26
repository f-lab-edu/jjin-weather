package com.jin.jjinweather.layer.domain.usecase

import com.jin.jjinweather.layer.domain.model.weather.Weather
import com.jin.jjinweather.layer.domain.repository.WeatherRepository

class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(): Weather {
        return repository.loadWeather()
    }
}
