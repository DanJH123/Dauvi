package com.dapps.dauvi.feature_settings.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.dapps.dauvi.core.util.LocationDs

data class SettingsState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val location: LocationDs? = null,
    val highUviValue: Int = 3,
    val dailyUviAlertOn: Boolean = false,
    val dailyUviAlertTimeValue: TimePickerState = TimePickerState(8, 0, true),
    val highUviAlertOn: Boolean = false,
    val highUviAlertValue: Int = 3
)