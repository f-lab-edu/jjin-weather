package com.jin.jjinweather.feature.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jin.jjinweather.feature.weather.domain.model.DailyForecast
import com.jin.jjinweather.feature.weather.domain.model.DailyWeather
import com.jin.jjinweather.feature.weather.domain.model.HourlyForecast
import com.jin.jjinweather.feature.weather.domain.model.HourlyWeather
import com.jin.jjinweather.feature.weather.domain.model.MoonPhaseType

/**
 * 위치 기반 날씨 정보
 *
 * @property id 자동 증가되는 시퀀스 넘버
 * @property locationIndex  (TODO TemperatureScreen 정의 후 추가 될 예정)
 * - 이 날씨 정보가 어떤 위치(`GeoPointEntity`)에 해당하는지 구분하는 식별자입니다.
 * - 같은 locationIndex 값을 가진 GeoPointEntity와 매칭되어, 위치와 날씨가 연결됩니다.
 * @property iconCode 날씨 상태를 나타내는 아이콘 코드
 * @property currentTemperature 현재 기온
 * @property yesterdayTemperature 어제 기온
 * @property minTemperature 오늘의 최저 기온
 * @property maxTemperature 오늘의 최고 기온
 * @property hourlyWeatherList 시간별 날씨 리스트
 * @property dailyWeatherList 일별 날씨 리스트
 * @property sunrise 일출 시각 (Unix time)
 * @property sunset 일몰 시각 (Unix time)
 * @property moonPhase 달 위상 (0.0 ~ 1.0)
 */
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val iconCode: String,
    val currentTemperature: Double,
    val temperatureDescription: String,
    val yesterdayTemperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val hourlyWeatherList: HourlyForecast,
    val dailyWeatherList: List<DailyForecast>,
    val sunrise: Long,
    val sunset: Long,
    val feelsLikeTemperature: Double,
    val moonPhase: MoonPhaseType
)
