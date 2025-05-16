package com.jin.jjinweather.feature.outfit.data.model

import com.google.gson.annotations.SerializedName

data class DalleRequest(
    val model: String,
    val prompt: String,
    val n: Int,
    val size: String
)

data class DalleResponse(
    val created: Long,
    val data: List<DalleImageData>
)

data class DalleImageData(
    @SerializedName("revised_prompt") val revisedPrompt: String,
    val url: String
)
