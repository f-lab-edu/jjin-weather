package com.jin.jjinweather.feature.outfit.domain

class GetOutfitUseCase(private val outfitRepository: OutfitRepository) {
    suspend operator fun invoke(temperature: Int): Result<String> {
        return outfitRepository.generateOutfitImage(temperature)
    }
}
