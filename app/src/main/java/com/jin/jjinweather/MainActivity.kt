package com.jin.jjinweather

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.google.gson.reflect.TypeToken
import com.jin.jjinweather.feature.datastore.data.PreferencesRepositoryImpl
import com.jin.jjinweather.feature.location.LocationRepository
import com.jin.jjinweather.feature.location.data.LocationRepositoryImpl
import com.jin.jjinweather.feature.locationimpl.data.GeoCodeDataSourceImpl
import com.jin.jjinweather.feature.locationimpl.data.GeoPointDataSourceImpl
import com.jin.jjinweather.feature.navigation.Screens
import com.jin.jjinweather.feature.network.NetworkProvider
import com.jin.jjinweather.feature.network.OpenAiApiClient
import com.jin.jjinweather.feature.network.OpenWeatherApiClient
import com.jin.jjinweather.feature.district.ui.DistrictSearchScreen
import com.jin.jjinweather.feature.onboarding.ui.OnboardingScreen
import com.jin.jjinweather.feature.onboarding.ui.OnboardingViewModel
import com.jin.jjinweather.feature.outfit.data.OutfitRepositoryImpl
import com.jin.jjinweather.feature.outfit.domain.GetOutfitUseCase
import com.jin.jjinweather.feature.outfit.domain.HourlyForecastGraph
import com.jin.jjinweather.feature.outfit.domain.OutfitArguments
import com.jin.jjinweather.feature.outfit.domain.OutfitRepository
import com.jin.jjinweather.feature.outfit.domain.toHourlyForecast
import com.jin.jjinweather.feature.outfit.domain.toHourlyForecastGraph
import com.jin.jjinweather.feature.outfitImpl.DalleDataSourceImpl
import com.jin.jjinweather.feature.outfitImpl.OpenAiDataSourceImpl
import com.jin.jjinweather.feature.outfitrecommend.ui.OutfitScreen
import com.jin.jjinweather.feature.outfitrecommend.ui.OutfitViewModel
import com.jin.jjinweather.feature.temperature.ui.TemperatureScreen
import com.jin.jjinweather.feature.temperature.ui.TemperatureViewModel
import com.jin.jjinweather.feature.weather.data.WeatherRepositoryImpl
import com.jin.jjinweather.feature.weather.domain.repository.WeatherRepository
import com.jin.jjinweather.feature.weather.domain.usecase.GetCurrentLocationWeatherUseCase
import com.jin.jjinweather.feature.weatherimpl.data.WeatherDataSourceImpl
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

        val openWeatherApi = OpenWeatherApiClient.createService()
        val chatGptApi = OpenAiApiClient.createService(BuildConfig.CHAT_GPT_API_KEY)

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
                            chatGPTApi = chatGptApi
                        ),
                        dalleDataSource = DalleDataSourceImpl(
                            chatGPTApi = chatGptApi
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
                onNavigateToOutfit = { temperature, cityName, summary, hourlyForecast, feelsLikeTemperature ->
                    val forecastJson = NetworkProvider.gson.toJson(hourlyForecast.map { it.toHourlyForecastGraph() })
                    val route = Screens.Outfit.createRoute(
                        temperature, cityName, summary, forecastJson, feelsLikeTemperature
                    )
                    navController.navigate(route)
                },
                onNavigateToDistrict = {
                    navController.navigate(Screens.DistrictSearch.route)
                }
            )
        }
        composable(
            route = Screens.Outfit.route,
            arguments = listOf(
                navArgument(Screens.TEMPERATURE) { type = NavType.IntType },
                navArgument(Screens.CITY_NAME) { type = NavType.StringType },
                navArgument(Screens.WEATHER_SUMMARY) { type = NavType.StringType },
                navArgument(Screens.HOURLY_FORECAST) { type = NavType.StringType },
                navArgument(Screens.FEELS_LIKE_TEMPERATURE) { type = NavType.IntType },
            )
        ) { backStackEntry ->
            // FIXME : key 값으로 DB 조회하는 방향으로 수정 예정
            val args = backStackEntry.parseOutfitArguments()
            OutfitScreen(
                viewModel = outfitViewModel,
                temperature = args.temperature,
                cityName = args.cityName,
                summary = args.weatherSummary,
                forecast = args.hourlyForecast,
                feelsLikeTemperature = args.feelsLikeTemperature
            ) {
                navController.navigate(Screens.Temperature.route)
            }
        }
        composable(Screens.DistrictSearch.route) {
            DistrictSearchScreen() {
                navController.navigate(Screens.Temperature.route)
            }
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

fun NavBackStackEntry.parseOutfitArguments(): OutfitArguments {
    val args = arguments ?: return OutfitArguments(0, "", "", listOf(), 0)
    val temp = args.getInt(Screens.TEMPERATURE)
    val city = args.getString(Screens.CITY_NAME).orEmpty()
    val summary = args.getString(Screens.WEATHER_SUMMARY).orEmpty()
    val forecastJson = args.getString(Screens.HOURLY_FORECAST).orEmpty()
    val feelsLikeTemperature = args.getInt(Screens.FEELS_LIKE_TEMPERATURE)

    val listType = object : TypeToken<List<HourlyForecastGraph>>() {}.type
    val parsed = NetworkProvider.gson.fromJson<List<HourlyForecastGraph>>(forecastJson, listType)
    val forecast = parsed.map { it.toHourlyForecast() }

    return OutfitArguments(temp, city, summary, forecast, feelsLikeTemperature)
}
