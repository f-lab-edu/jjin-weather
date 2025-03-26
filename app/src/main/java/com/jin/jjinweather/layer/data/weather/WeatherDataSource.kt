package com.jin.jjinweather.layer.data.weather

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherDataSource(private val context: Context) {
    suspend fun loadWeatherJson(): String {
        return withContext(Dispatchers.IO) {
            context.assets.open("weather.json").bufferedReader().use { it.readText() }
        }
    }
}
