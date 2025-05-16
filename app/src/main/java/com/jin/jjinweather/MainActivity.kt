package com.jin.jjinweather

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.jin.jjinweather.feature.location.LocationRepository
import com.jin.jjinweather.feature.location.data.LocationRepositoryImpl
import com.jin.jjinweather.feature.locationimpl.data.GeoCodeDataSourceImpl
import com.jin.jjinweather.feature.locationimpl.data.GeoPointDataSourceImpl
import com.jin.jjinweather.feature.weather.domain.repository.WeatherRepository
import com.jin.jjinweather.feature.weather.data.OpenWeatherApi
import com.jin.jjinweather.feature.weather.data.WeatherRepositoryImpl
import com.jin.jjinweather.feature.weatherimpl.data.WeatherDataSourceImpl
import com.jin.jjinweather.feature.network.RetrofitClient
import com.jin.jjinweather.feature.datastore.data.PreferencesRepositoryImpl
import com.jin.jjinweather.feature.weather.domain.usecase.GetCurrentLocationWeatherUseCase
import com.jin.jjinweather.feature.navigation.Screens
import com.jin.jjinweather.feature.onboarding.ui.OnboardingScreen
import com.jin.jjinweather.feature.onboarding.ui.OnboardingViewModel
import com.jin.jjinweather.feature.outfit.data.ChatGptApi
import com.jin.jjinweather.feature.outfit.data.OutfitRepositoryImpl
import com.jin.jjinweather.feature.outfit.domain.GetOutfitUseCase
import com.jin.jjinweather.feature.outfit.domain.OutfitRepository
import com.jin.jjinweather.feature.outfitImpl.DalleDataSourceImpl
import com.jin.jjinweather.feature.outfitImpl.OpenAiDataSourceImpl
import com.jin.jjinweather.feature.outfitrecommend.ui.OutfitScreen
import com.jin.jjinweather.feature.outfitrecommend.ui.OutfitViewModel
import com.jin.jjinweather.feature.temperature.ui.TemperatureScreen
import com.jin.jjinweather.feature.temperature.ui.TemperatureViewModel
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

        val openWeatherApi: OpenWeatherApi =
            RetrofitClient.createService("https://api.openweathermap.org/data/3.0/")
        val chatGptApi: ChatGptApi = RetrofitClient.createService("https://api.openai.com/")

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "weather_db").build()

        enableEdgeToEdge()
        setContent {
            JJinWeatherTheme {
                AppNavigator(
                    context = this,
                    locationRepository = LocationRepositoryImpl(
                        db.geoPointTrackingDataSource(),
                        db.cityNameTrackingDataSource(),
                        GeoPointDataSourceImpl(this),
                        GeoCodeDataSourceImpl(this),
                    ),
                    weatherRepository = WeatherRepositoryImpl(
                        db.weatherTrackingDataSource(),
                        WeatherDataSourceImpl(openWeatherApi, BuildConfig.OPEN_WEATHER_API_KEY),
                    ),
                    outfitRepository = OutfitRepositoryImpl(
                        openAiDataSource = OpenAiDataSourceImpl(
                            chatGPTApi = chatGptApi,
                            gptApiKey = BuildConfig.CHAT_GPT_API_KEY
                        ),
                        dalleDataSource = DalleDataSourceImpl(
                            chatGPTApi = chatGptApi,
                            gptApiKey = BuildConfig.CHAT_GPT_API_KEY
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun AppNavigator(
    context: Context,
    locationRepository: LocationRepository,
    weatherRepository: WeatherRepository,
    outfitRepository: OutfitRepository,
) {
    val navController = rememberNavController()

    val onboardingViewModel = OnboardingViewModel(PreferencesRepositoryImpl(context))
    val temperatureViewModel = TemperatureViewModel(
        GetCurrentLocationWeatherUseCase(locationRepository, weatherRepository)
    )
    val outfitViewModel = OutfitViewModel(GetOutfitUseCase(outfitRepository))

    NavHost(navController, Screens.Onboarding.route) {
        composable(Screens.Onboarding.route) {
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onNavigateToTemperature = {
                    navController.navigateClearingBackStack(
                        destination = Screens.Temperature,
                        clearUpTo = Screens.Onboarding,
                        inclusive = true
                    )
                }
            )
        }
        composable(Screens.Temperature.route) {
            TemperatureScreen(
                viewModel = temperatureViewModel,
                onNavigateToOutfit = { temperature ->
                    navController.navigate(Screens.Outfit.createRoute(temperature))
                }
            )
        }
        composable(
            route = Screens.Outfit.route,
            arguments = listOf(navArgument("temperature") { type = NavType.IntType })
        ) { backStackEntry ->
            val temperature = backStackEntry.arguments?.getInt("temperature") ?: 0
            OutfitScreen(viewModel = outfitViewModel, temperature = temperature)
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
