package com.jin.jjinweather.layer.data.repository

import com.jin.jjinweather.layer.data.location.LocationProvider
import com.jin.jjinweather.layer.data.weather.WeatherDataSource
import com.jin.jjinweather.layer.domain.model.weather.DailyWeather
import com.jin.jjinweather.layer.domain.model.weather.HourlyWeather
import com.jin.jjinweather.layer.domain.model.weather.Weather
import com.jin.jjinweather.layer.domain.repository.WeatherRepository
import org.json.JSONObject

class WeatherRepositoryImpl(
    private val weatherDataSource: WeatherDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    override suspend fun loadWeather(): Weather {
        val json = weatherDataSource.loadWeatherJson()
        val root = JSONObject(json)

        val current = root.getJSONObject("current")
        val dailyArray = root.getJSONArray("daily")
        val hourlyArray = root.getJSONArray("hourly")

        val cityName = locationProvider.loadCurrentCityName()
        val iconResId = 1 // todo: icon 매핑 함수 만들기
        val currentTemp = current.getDouble("temp").toInt()
        val yesterdayTemp = currentTemp // todo: 실제 전날 데이터 추가 시 분리
        val sunrise = current.getLong("sunrise")
        val sunset = current.getLong("sunset")
        val moonPhase = dailyArray.getJSONObject(0).getDouble("moon_phase")

        val tempObj = dailyArray.getJSONObject(0).getJSONObject("temp")
        val minTemp = tempObj.getDouble("min").toInt()
        val maxTemp = tempObj.getDouble("max").toInt()

        val hourlyList = (0 until hourlyArray.length()).map { i ->
            hourlyArray.getJSONObject(i).toHourlyWeather()
        }

        val dailyList = (0 until dailyArray.length()).map { i ->
            dailyArray.getJSONObject(i).toDailyWeather()
        }

        return Weather(
            cityName = cityName,
            iconResId = iconResId,
            currentTemperature = currentTemp,
            yesterdayTemperature = yesterdayTemp,
            minTemperature = minTemp,
            maxTemperature = maxTemp,
            hourlyWeatherList = hourlyList,
            dailyWeatherList = dailyList,
            sunrise = sunrise,
            sunset = sunset,
            moonPhase = moonPhase
        ).also {
            println("weather: $it")
        }
    }

    private fun JSONObject.toHourlyWeather(): HourlyWeather {
        val hour = getLong("dt")
        val temp = getDouble("temp").toInt()
        val icon = 1 // todo: icon 매핑 함수 만들기
        return HourlyWeather(hour, icon, temp)
    }

    private fun JSONObject.toDailyWeather(): DailyWeather {
        val day = getLong("dt")
        val tempObj = getJSONObject("temp")
        val min = tempObj.getDouble("min").toInt()
        val max = tempObj.getDouble("max").toInt()
        val icon = 1 // todo: icon 매핑 함수 만들기
        return DailyWeather(day, icon, min, max)
    }
}
