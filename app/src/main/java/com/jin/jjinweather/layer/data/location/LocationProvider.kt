package com.jin.jjinweather.layer.data.location

import android.content.Context

class LocationProvider(private val context: Context) {
    suspend fun loadCurrentCityName(): String {
        // 실제 권한 요청 + 위치 정보 가져오기
        return "서울시 방배동"
    }
}
