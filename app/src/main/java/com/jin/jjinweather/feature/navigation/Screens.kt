package com.jin.jjinweather.feature.navigation

import android.net.Uri

sealed class Screens(val route: String) {
    object Onboarding : Screens("onboardingScreen")
    object Temperature : Screens("temperatureScreen")
    object Outfit : Screens(
        "outfitScreen/{$TEMPERATURE}/{$CITY_NAME}/{$WEATHER_SUMMARY}/{$HOURLY_FORECAST}/{$FEELS_LIKE_TEMPERATURE}"
    ) {
        // FIXME : key 값으로 DB 조회하는 방향으로 수정 예정
        fun createRoute(
            temperature: Int,
            cityName: String,
            weatherSummary: String,
            forecastJson: String,
            feelsLikeTemperature: Int
        ): String {
            val forecastEncoded = Uri.encode(forecastJson)
            return "outfitScreen/$temperature/$cityName/$weatherSummary/$forecastEncoded/$feelsLikeTemperature"
        }
    }

    companion object {
        const val TEMPERATURE = "temperature"
        const val CITY_NAME = "cityName"
        const val WEATHER_SUMMARY = "weatherSummary"
        const val HOURLY_FORECAST = "hourlyForecast"
        const val FEELS_LIKE_TEMPERATURE = "feelsLikeTemperature"
    }
}
