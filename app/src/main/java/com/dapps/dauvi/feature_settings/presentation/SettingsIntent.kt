package com.dapps.dauvi.feature_settings.presentation


sealed class SettingsIntent {
    data object LoadSettings : SettingsIntent() //todo: use this in viewmodel
    data class SetHighUviValue(val highUviValue: Int) : SettingsIntent()
    data class SetDailyUviAlertOn(val dailyUviAlertOn: Boolean) : SettingsIntent()
    data class SetDailyUviAlertTimeValue(val hour: Int, val minute: Int) : SettingsIntent()
    data class SetHighUviAlertOn(val highUviAlertOn: Boolean) : SettingsIntent()
    data class SetHighUviAlertValue(val highUviAlertValue: Int) : SettingsIntent()
    data class OpenUrl(val url: String) : SettingsIntent()
    data object NavigateToTerms : SettingsIntent()
    data object RequestLocation : SettingsIntent()
    data object RequestLocationPermission : SettingsIntent()
}