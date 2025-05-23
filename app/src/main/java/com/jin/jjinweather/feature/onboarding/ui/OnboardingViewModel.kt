package com.jin.jjinweather.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.jjinweather.feature.datastore.PreferencesRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: PreferencesRepository) : ViewModel() {
    private var isFirstLaunch = true

    init {
        checkIfFirstLaunch()
    }

    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            if (isFirstLaunch) {
                repository.completeFirstLaunch()
            }
        }
    }

    private fun checkIfFirstLaunch() {
        viewModelScope.launch {
            isFirstLaunch = repository.isFirstLaunch()
        }
    }

    companion object {
        private const val TAG = "OnboardingViewModel"
    }
}
