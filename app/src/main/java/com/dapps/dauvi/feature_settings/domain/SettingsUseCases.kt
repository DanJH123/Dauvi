package com.dapps.dauvi.feature_settings.domain

import com.dapps.dauvi.core.util.LocationDs
import kotlinx.coroutines.flow.Flow

data class SettingsUseCases(
    val updateLocation: UpdateLocationUseCase,
    val getLocationFlow: GetLocationFlowUseCase,
    val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    val setHighUviValue: SetHighUviValueUseCase,
    val setDailyUviAlertOn: SetDailyUviAlertOnUseCase,
    val setDailyUviAlertTimeValue: SetDailyUviAlertTimeValueUseCase,
    val setHighUviAlertOn: SetHighUviAlertOnUseCase,
    val setHighUviAlertValue: SetHighUviAlertValueUseCase,
    val getHighUviValueFlow: GetHighUviValueFlowUseCase,
    val getDailyUviAlertOnFlow: GetDailyUviAlertOnFlowUseCase,
    val getDailyUviAlertTimeValueFlow: GetDailyUviAlertTimeValueFlowUseCase,
    val getHighUviAlertOnFlow: GetHighUviAlertOnFlowUseCase,
    val getHighUviAlertValueFlow: GetHighUviAlertValueFlowUseCase
)


class UpdateLocationUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(locationDs: LocationDs) {
        repository.updateLocation(locationDs)
    }
}

class SetHighUviValueUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(highUviValue: Int) {
        repository.setHighUviValue(highUviValue)
    }
}

class SetDailyUviAlertOnUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(dailyUviAlertOn: Boolean) {
        repository.setDailyUviAlertOn(dailyUviAlertOn)
    }
}

class SetDailyUviAlertTimeValueUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(dailyUviAlertTimeValue: Long) {
        repository.setDailyUviAlertTimeValue(dailyUviAlertTimeValue)
    }
}

class SetHighUviAlertOnUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(highUviAlertOn: Boolean) {
        repository.setHighUviAlertOn(highUviAlertOn)
    }
}

class SetHighUviAlertValueUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(highUviAlertValue: Int) {
        repository.setHighUviAlertValue(highUviAlertValue)
    }
}

class GetLocationFlowUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<LocationDs?> = repository.locationFlow
}

class GetHighUviValueFlowUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int?> = repository.highUviValueFlow
}

class GetDailyUviAlertOnFlowUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean?> = repository.dailyUviAlertOnFlow
}

class GetDailyUviAlertTimeValueFlowUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Long?> = repository.dailyUviAlertTimeValueFlow
}

class GetHighUviAlertOnFlowUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean?> = repository.highUviAlertOnFlow
}

class GetHighUviAlertValueFlowUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Int?> = repository.highUviAlertValueFlow
}

