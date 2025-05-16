package com.jin.jjinweather.feature.weather.data

import com.jin.jjinweather.feature.weather.data.model.dto.WeatherDTO
import com.jin.jjinweather.feature.weather.data.model.dto.HistoricalWeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    /**
     * 현재 위치 기반의 날씨 정보를 가져오는 API 호출.
     * @param latitude 위도
     * @param longitude 경도
     * @param exclude 제외할 데이터 (예: "minutely")
     * @param units 온도 단위 (예: "metric" 섭씨 , "imperial" 화씨)
     * @param lang 응답 언어 (예: "kr", "en")
     * @param apiKey OpenWeatherMap API 키
     * @return 날씨 정보가 담긴 [WeatherDTO]
     */
    @GET("onecall")
    suspend fun queryWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String
    ): WeatherDTO

    /**
     * 특정 위치의 이전 날씨(히스토리컬 데이터)를 가져오는 API 호출.
     * @param latitude 위도
     * @param longitude 경도
     * @param dateTime 조회할 날짜 및 시간 (Unix Time - 초 단위)
     * @param units 온도 단위 (예: "metric" 섭씨 , "imperial" 화씨)
     * @param lang 응답 언어 (예: "kr", "en")
     * @param apiKey OpenWeatherMap API 키
     * @return 이전 날씨 정보가 담긴 [HistoricalWeatherDto]
     */
    @GET("onecall/timemachine")
    suspend fun queryHistoricalWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("dt") dateTime: Long,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String
    ): HistoricalWeatherDto
}
