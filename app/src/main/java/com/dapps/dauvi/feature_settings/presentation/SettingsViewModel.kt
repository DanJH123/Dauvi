package com.dapps.dauvi.feature_settings.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapps.dauvi.core.util.LocationDs
import com.dapps.dauvi.feature_settings.domain.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val settingsFlows = listOf(
        settingsUseCases.getLocationFlow(),
        settingsUseCases.getHighUviValueFlow(),
        settingsUseCases.getDailyUviAlertOnFlow(),
        settingsUseCases.getDailyUviAlertTimeValueFlow(),
        settingsUseCases.getHighUviAlertOnFlow(),
        settingsUseCases.getHighUviAlertValueFlow()
    )

    val state: StateFlow<SettingsState> = combine(settingsFlows) { arrayOfValues ->
        val location = arrayOfValues[0] as LocationDs?
        val highUviValue = (arrayOfValues[1] as Int?) ?: 3
        val dailyUviAlertOn = (arrayOfValues[2] as Boolean?) ?: false
        val dailyUviAlertTimeValue = (arrayOfValues[3] as Long?)
        val dailyUviAlertTimeState = if(dailyUviAlertTimeValue != null) {
            val hour = (dailyUviAlertTimeValue / 1000 / 60 / 60).toInt()
            val minute = ((dailyUviAlertTimeValue / 1000 / 60) % 60).toInt()
            TimePickerState(hour, minute, true)
        } else {
            TimePickerState(8, 0, true)
        }
        val highUviAlertOn = (arrayOfValues[4] as Boolean?) ?: false
        val highUviAlertValue = (arrayOfValues[5] as Int?) ?: 3
        SettingsState(
            location = location,
            highUviValue = highUviValue,
            dailyUviAlertOn = dailyUviAlertOn,
            dailyUviAlertTimeValue = dailyUviAlertTimeState,
            highUviAlertOn = highUviAlertOn,
            highUviAlertValue = highUviAlertValue
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()


    @SuppressLint("MissingPermission")
    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.RequestLocation -> {
                    viewModelScope.launch {
                        _events.emit(SettingsEvent.LocationRequestLoading)
                        val currentLocation = settingsUseCases.getCurrentLocationUseCase()
                        currentLocation?.let {
                            settingsUseCases.updateLocation(it)
                            // todo - reset weather data
                            _events.emit(SettingsEvent.LocationRequestCompleted)
                        } ?: _events.emit(SettingsEvent.LocationRequestFailed)
                    }
                }
                is SettingsIntent.RequestLocationPermission -> {
                    _events.emit(SettingsEvent.RequestLocationPermission)
                }
                is SettingsIntent.SetHighUviValue -> {
                    settingsUseCases.setHighUviValue(intent.highUviValue)
                }
                is SettingsIntent.SetDailyUviAlertOn -> {
                    settingsUseCases.setDailyUviAlertOn(intent.dailyUviAlertOn)
                }
                is SettingsIntent.SetDailyUviAlertTimeValue -> {
                    val millis = intent.hour * 60 * 60 * 1000L + intent.minute * 60 * 1000L
                    settingsUseCases.setDailyUviAlertTimeValue(millis)
                }
                is SettingsIntent.SetHighUviAlertOn -> {
                    settingsUseCases.setHighUviAlertOn(intent.highUviAlertOn)
                }
                is SettingsIntent.SetHighUviAlertValue -> {
                    settingsUseCases.setHighUviAlertValue(intent.highUviAlertValue)
                }
                is SettingsIntent.OpenUrl -> {
                    _events.emit(SettingsEvent.OpenUrl(intent.url))
                }
                is SettingsIntent.NavigateToTerms -> {
                    _events.emit(SettingsEvent.NavigateToTerms)
                }
                else -> Unit
            }
        }
    }
}
