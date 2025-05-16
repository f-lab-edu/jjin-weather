package com.jin.jjinweather.feature.weather.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jin.jjinweather.R
import java.time.Instant
import java.time.LocalTime
import java.util.Calendar

data class Weather(
    val dayWeather: DayWeather,
    val yesterdayWeather: TemperatureSnapshot,
    val forecast: Forecast
)

data class DayWeather(
    val date: Calendar,
    val icon: WeatherIcon,
    val temperature: Number,
    val description: String,
    val sunCycle: SunCycle,
    val feelsLikeTemperature: Number,
    val moonPhase: MoonPhaseType,
    val temperatureRange: TemperatureRange
)

data class SunCycle(
    val sunrise: LocalTime,
    val sunset: LocalTime
)

data class TemperatureSnapshot(
    val timeStamp: Instant,
    val icon: WeatherIcon,
    val temperature: Number,
)

data class Forecast(
    val hourly: HourlyForecast,
    val daily: List<DailyForecast>
)

typealias HourlyForecast = List<TemperatureSnapshot>

data class DailyForecast(
    val date: Calendar,
    val icon: WeatherIcon,
    val temperatureRange: TemperatureRange,
    val sunCycle: SunCycle,
    val feelsLikeTemperatureRange: FeelsLikeTemperatureRange,
    val summary: String
)

data class TemperatureRange(
    val min: Number,
    val max: Number
)

data class FeelsLikeTemperatureRange(
    val dayTemperature: Number,
    val nightTemperature: Number
)

enum class WeatherIcon(@DrawableRes val drawableRes: Int) {
    CLEAR_SKY_DAY(R.drawable.ic_main_clear_sky_day),
    CLEAR_SKY_NIGHT(R.drawable.ic_main_clear_sky_night),
    FEW_CLOUDS_DAY(R.drawable.ic_main_few_clouds_day),
    FEW_CLOUDS_NIGHT(R.drawable.ic_main_few_clouds_night),
    SCATTERED_CLOUDS(R.drawable.ic_main_scattered_clouds),
    SHOWER_RAIN(R.drawable.ic_main_shower_rain),
    RAIN_DAY(R.drawable.ic_main_rain_day),
    RAIN_NIGHT(R.drawable.ic_main_rain_night),
    THUNDERSTORM(R.drawable.ic_main_thunderstorm),
    SNOW(R.drawable.ic_main_snow),
    MIST(R.drawable.ic_main_mist);

    companion object {
        fun findByWeatherCode(code: String): WeatherIcon {
            return when (code) {
                "01d" -> CLEAR_SKY_DAY
                "01n" -> CLEAR_SKY_NIGHT
                "02d" -> FEW_CLOUDS_DAY
                "02n" -> FEW_CLOUDS_NIGHT
                "03d", "03n", "04d", "04n" -> SCATTERED_CLOUDS
                "09d", "09n" -> SHOWER_RAIN
                "10d" -> RAIN_DAY
                "10n" -> RAIN_NIGHT
                "11d", "11n" -> THUNDERSTORM
                "13d", "13n" -> SNOW
                "50d", "50n" -> MIST
                else -> CLEAR_SKY_DAY
            }
        }
    }
}

enum class MoonPhaseType(
    @DrawableRes val iconDrawableRes: Int,
    @StringRes val nameStringRes: Int
) {
    NEW_MOON(R.drawable.ic_mew_moon, R.string.moon_phase_new_moon),
    START_CRESCENT_MOON(R.drawable.ic_start_crescent_moon, R.string.moon_phase_crescent_moon),
    START_HALF_MOON(R.drawable.ic_start_half_moon, R.string.moon_phase_half_moon),
    BEFORE_FULL_MOON(R.drawable.ic_before_full_moon, R.string.moon_phase_before_full_moon),
    FULL_MOON(R.drawable.ic_full_moon, R.string.moon_phase_full_moon),
    AFTER_FULL_MOON(R.drawable.ic_after_full_moon, R.string.moon_phase_after_full_moon),
    END_HALF_MOON(R.drawable.ic_end_half_moon, R.string.moon_phase_half_moon),
    END_CRESCENT_MOON(R.drawable.ic_end_crescent_moon, R.string.moon_phase_crescent_moon);

    companion object {
        fun findByMoonPhase(phase: Double): MoonPhaseType {
            val phaseInt = (phase * 100).toInt()
            return when (phaseInt) {
                in 0..3, 100 -> NEW_MOON
                in 4..24 -> START_CRESCENT_MOON
                25 -> START_HALF_MOON
                in 26..49 -> BEFORE_FULL_MOON
                50 -> FULL_MOON
                in 51..74 -> AFTER_FULL_MOON
                75 -> END_HALF_MOON
                in 76..99 -> END_CRESCENT_MOON
                else -> NEW_MOON
            }
        }
    }
}
