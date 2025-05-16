package com.jin.jjinweather.feature.navigation

sealed class Screens(val route: String) {
    object Onboarding : Screens("onboardingScreen")
    object Temperature : Screens("temperatureScreen")
    object Outfit : Screens("outfitScreen/{temperature}") {
        fun createRoute(temperature: Int): String = "outfitScreen/$temperature"
    }
}
