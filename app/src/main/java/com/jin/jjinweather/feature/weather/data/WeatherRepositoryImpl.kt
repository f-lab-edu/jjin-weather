package com.jin.jjinweather.feature.weather.data

import android.database.SQLException
import com.jin.jjinweather.feature.weather.data.model.WeatherEntity
import com.jin.jjinweather.feature.weather.domain.model.DayWeather
import com.jin.jjinweather.feature.weather.domain.model.Forecast
import com.jin.jjinweather.feature.weather.domain.model.SunCycle
import com.jin.jjinweather.feature.weather.domain.model.TemperatureRange
import com.jin.jjinweather.feature.weather.domain.model.TemperatureSnapshot
import com.jin.jjinweather.feature.weather.domain.model.Weather
import com.jin.jjinweather.feature.weather.domain.model.WeatherIcon
import com.jin.jjinweather.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalTime
import java.util.Calendar

class WeatherRepositoryImpl(
    private val weatherTrackingDataSource: WeatherTrackingDataSource,
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {

    override suspend fun weatherAt(latitude: Double, longitude: Double): Result<Weather> =
        weatherDataSource.requestWeatherAt(latitude, longitude)
            .onSuccess { keepTrackWeatherChanges(it) }
            .map { Result.success(it) }
            .getOrElse {
                val weather = queryLatestWeather() ?: return Result.failure(it)
                return Result.success(weather.toDomainModel())
            }


    private suspend fun queryLatestWeather(): WeatherEntity? {
        return try {
            withContext(Dispatchers.IO) {
                weatherTrackingDataSource.latestWeatherOrNull()
            }
        } catch (_: SQLException) {
            null
        }
    }

    private suspend fun keepTrackWeatherChanges(weather: Weather) {
        try {
            withContext(Dispatchers.IO) {
                weatherTrackingDataSource.markAsLatestWeather(weather.toEntityModel())
            }
        } catch (_: Exception) {
            // Silently ignore the error.
        }
    }

    /**
     * Domain layer Model -> Data layer Model 변환
     * */
    private fun Weather.toEntityModel(): WeatherEntity {
        return WeatherEntity(
            iconCode = dayWeather.icon.name,
            currentTemperature = dayWeather.temperature.toDouble(),
            temperatureDescription = dayWeather.description,
            yesterdayTemperature = yesterdayWeather.temperature.toDouble(),
            minTemperature = dayWeather.temperatureRange.min.toDouble(),
            maxTemperature = dayWeather.temperatureRange.max.toDouble(),
            hourlyWeatherList = forecast.hourly,
            dailyWeatherList = forecast.daily,
            sunrise = dayWeather.sunCycle.sunrise.toSecondOfDay().toLong(),
            sunset = dayWeather.sunCycle.sunset.toSecondOfDay().toLong(),
            feelsLikeTemperature = dayWeather.feelsLikeTemperature.toDouble(),
            moonPhase = dayWeather.moonPhase
        )
    }

    /**
     * Data layer Model -> Domain layer Model 변환
     * */
    private fun WeatherEntity.toDomainModel(): Weather {
        return Weather(
            dayWeather = DayWeather(
                date = Calendar.getInstance(),
                icon = WeatherIcon.valueOf(iconCode),
                temperature = currentTemperature,
                description = temperatureDescription,
                sunCycle = SunCycle(LocalTime.ofSecondOfDay(sunrise), LocalTime.ofSecondOfDay(sunset)),
                feelsLikeTemperature = feelsLikeTemperature,
                moonPhase = moonPhase,
                temperatureRange = TemperatureRange(min = minTemperature, max = maxTemperature)
            ),
            yesterdayWeather = TemperatureSnapshot(
                timeStamp = Instant.now(),
                icon = WeatherIcon.valueOf(iconCode),
                temperature = yesterdayTemperature
            ),
            forecast = Forecast(hourly = hourlyWeatherList, daily = dailyWeatherList)

        )
    }

    private companion object {
        const val TAG = "WeatherRepository"
    }
}
