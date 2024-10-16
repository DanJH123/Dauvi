package com.dapps.dauvi.feature_uvi.presentation.uvi

import com.dapps.dauvi.core.util.text.UiText

sealed class UviUiState {
    data object Initial: UviUiState()
    data object FetchingWeather: UviUiState()
    data object WeatherLoaded: UviUiState()

    data class WeatherLoadFail(val message: UiText): UviUiState(){

    }
}