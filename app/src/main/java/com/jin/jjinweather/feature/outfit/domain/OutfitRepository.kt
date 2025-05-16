package com.jin.jjinweather.feature.outfit.domain

interface OutfitRepository {
    suspend fun generateOutfitImage(temperature: Int): Result<String>
}
