package com.jin.jjinweather.feature.outfitImpl

import com.jin.jjinweather.feature.outfit.data.ChatGptApi
import com.jin.jjinweather.feature.outfit.data.OpenAiDataSource
import com.jin.jjinweather.feature.outfit.data.model.OpenAiChat
import com.jin.jjinweather.feature.outfit.data.model.OpenAiRequest

class OpenAiDataSourceImpl(
    private val chatGPTApi: ChatGptApi,
    private val gptApiKey: String,
) : OpenAiDataSource {

    override suspend fun generateImagePrompt(temperature: Int): Result<String> {
        val requestPrompt = buildClothingImagePrompt(temperature)
        val openAiRequest = OpenAiRequest(
            model = MODEL,
            messages = listOf(
                OpenAiChat(
                    role = ROLE_SYSTEM,
                    content = CONTENT
                ),
                OpenAiChat(role = ROLE_USER, content = requestPrompt)
            )
        )
        val openAiResponse = chatGPTApi.queryOpenAiPrompt("Bearer $gptApiKey", openAiRequest)
        val prompt = openAiResponse.choices.firstOrNull()?.message?.content
        return if (prompt.isNullOrEmpty()) {
            Result.failure(Exception())
        } else {
            Result.success(prompt)
        }
    }

    private fun buildClothingImagePrompt(temperature: Int): String = """
I need an English object description for a very simple clothing icon.
Draw a picture so that the top of the clothes and the bottom of the clothes are kept side by side without overlapping.
The icon should simply represent the top and bottom of the clothing suitable for ${temperature}°C weather.
The English object description must have a condition that the background color must be 0xFFFFFFFFFF, contain a very simple style, and not allow letters related to temperature.
""".trimIndent()

    private companion object {
        const val MODEL = "gpt-4o"
        const val ROLE_SYSTEM = "system"
        const val ROLE_USER = "user"
        const val CONTENT = "You're a stylist who describes outfits for icon generation."
    }
}
