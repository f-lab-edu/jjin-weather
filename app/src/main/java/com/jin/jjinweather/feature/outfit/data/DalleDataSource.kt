package com.jin.jjinweather.feature.outfit.data

interface DalleDataSource {
    suspend fun requestOutfitImageGeneration(prompt: String): Result<String>
}
