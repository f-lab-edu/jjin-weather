package com.jin.jjinweather.feature.outfit.data

interface OpenAiDataSource {
    suspend fun generateImagePrompt(temperature: Int): Result<String>
}
