package com.jin.jjinweather.feature.outfit.data

import com.jin.jjinweather.feature.outfit.domain.OutfitRepository

class OutfitRepositoryImpl(
    private val openAiDataSource: OpenAiDataSource,
    private val dalleDataSource: DalleDataSource
) : OutfitRepository {
    override suspend fun generateOutfitImage(temperature: Int): Result<String> {
        return openAiDataSource.generateImagePrompt(temperature).fold(
            onSuccess = {
                dalleDataSource.requestOutfitImageGeneration(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}
