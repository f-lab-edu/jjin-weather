package com.jin.jjinweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jin.jjinweather.layer.data.RetrofitClient
import com.jin.jjinweather.layer.data.location.LocationProvider
import com.jin.jjinweather.layer.data.repository.LocationRepositoryImpl
import com.jin.jjinweather.layer.data.repository.WeatherRepositoryImpl
import com.jin.jjinweather.layer.data.weather.WeatherDataSource
import com.jin.jjinweather.layer.data.weather.WeatherService
import com.jin.jjinweather.layer.domain.usecase.GetGeoPointUseCase
import com.jin.jjinweather.layer.domain.usecase.GetWeatherUseCase
import com.jin.jjinweather.layer.ui.Screens
import com.jin.jjinweather.layer.ui.onboarding.OnboardingScreen
import com.jin.jjinweather.layer.ui.onboarding.OnboardingViewModel
import com.jin.jjinweather.layer.ui.temperature.TemperatureScreen
import com.jin.jjinweather.layer.ui.temperature.TemperatureViewModel
import com.jin.jjinweather.ui.theme.JJinWeatherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch {
            delay(1000L)
            keepSplashScreen = false
        }
        val locationProvider = LocationProvider(this)
        val weatherService: WeatherService = RetrofitClient.createService("https://api.openweathermap.org/data/3.0/")
        val weatherDataSource = WeatherDataSource(weatherService, locationProvider)

        enableEdgeToEdge()
        setContent {
            JJinWeatherTheme {
                AppNavigator(weatherDataSource, locationProvider)
            }
        }
    }
}

@Composable
fun AppNavigator(weatherDataSource: WeatherDataSource, locationProvider: LocationProvider) {
    val navController = rememberNavController()

    val weatherRepository = WeatherRepositoryImpl(weatherDataSource)
    val locationRepository = LocationRepositoryImpl(locationProvider)

    val onboardingViewModel = OnboardingViewModel(
        GetWeatherUseCase(weatherRepository),
        GetGeoPointUseCase(locationRepository)
    )
    val temperatureViewModel = TemperatureViewModel()

    NavHost(navController, Screens.ONBOARDING.route) {
        composable(Screens.ONBOARDING.route) {
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onNavigateToTemperature = {
                    navController.navigateClearingBackStack(
                        destination = Screens.TEMPERATURE,
                        clearUpTo = Screens.ONBOARDING,
                        inclusive = true
                    )
                }
            )
        }
        composable(Screens.TEMPERATURE.route) {
            TemperatureScreen(
                viewModel = temperatureViewModel,
                onNavigate = {}
            )
        }
    }
}

fun NavController.navigateClearingBackStack(
    destination: Screens,
    clearUpTo: Screens,
    inclusive: Boolean
) {
    this.navigate(destination.route) {
        popUpTo(clearUpTo.route) {
            this.inclusive = inclusive
        }
    }
}
