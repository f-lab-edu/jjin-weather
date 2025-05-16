package com.jin.jjinweather.feature.outfitrecommend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.jjinweather.feature.outfit.domain.GetOutfitUseCase
import com.jin.jjinweather.feature.weather.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OutfitViewModel(private val getOutfitUseCase: GetOutfitUseCase) : ViewModel() {
    private val _outfitImageUrl = MutableStateFlow<UiState<String>>(UiState.Loading)
    val outfitImageUrl: StateFlow<UiState<String>> = _outfitImageUrl

    fun loadOutfitImageForTemperature(temperature: Int) {
        viewModelScope.launch {
            _outfitImageUrl.value = getOutfitUseCase(temperature).fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message.orEmpty()) }
            )
        }
    }
}
