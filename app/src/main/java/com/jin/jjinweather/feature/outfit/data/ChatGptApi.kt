package com.jin.jjinweather.feature.outfit.data

import com.jin.jjinweather.feature.outfit.data.model.DalleRequest
import com.jin.jjinweather.feature.outfit.data.model.DalleResponse
import com.jin.jjinweather.feature.outfit.data.model.OpenAiRequest
import com.jin.jjinweather.feature.outfit.data.model.OpenAiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatGptApi {
    @POST("v1/chat/completions")
    suspend fun queryOpenAiPrompt(
        @Header("Authorization") authorization: String,
        @Body body: OpenAiRequest
    ): OpenAiResponse

    @POST("v1/images/generations")
    suspend fun queryImageGeneration(
        @Header("Authorization") authorization: String,
        @Body body: DalleRequest
    ): DalleResponse
}
