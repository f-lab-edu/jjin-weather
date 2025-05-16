package com.jin.jjinweather.feature.outfitImpl

import com.jin.jjinweather.feature.outfit.data.ChatGptApi
import com.jin.jjinweather.feature.outfit.data.DalleDataSource
import com.jin.jjinweather.feature.outfit.data.model.DalleRequest

class DalleDataSourceImpl(
    private val chatGPTApi: ChatGptApi,
    private val gptApiKey: String,
) : DalleDataSource {

    override suspend fun requestOutfitImageGeneration(prompt: String): Result<String> {
        val dalleRequest = DalleRequest(
            model = MODEL,
            prompt = prompt,
            n = IMAGE_COUNT,
            size = IMAGE_SIZE
        )
        val response = chatGPTApi.queryImageGeneration("Bearer $gptApiKey", dalleRequest)
        val imageUrl = response.data.firstOrNull()?.url
        return if (imageUrl.isNullOrEmpty()) {
            Result.failure(Exception())
        } else {
            Result.success(imageUrl)
        }
    }

    private companion object {
        const val MODEL = "dall-e-3"
        const val IMAGE_COUNT = 1
        const val IMAGE_SIZE = "1792x1024"
    }
}
