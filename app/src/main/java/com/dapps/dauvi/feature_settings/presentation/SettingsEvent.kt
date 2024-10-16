package com.dapps.dauvi.feature_settings.presentation

sealed class SettingsEvent {
    data class OpenUrl(val url: String) : SettingsEvent()
    data object NavigateToTerms : SettingsEvent()
    data object RequestLocationPermission : SettingsEvent()
    data object LocationRequestLoading : SettingsEvent()
    data object LocationRequestFailed : SettingsEvent()
    data object LocationRequestCompleted : SettingsEvent()
}