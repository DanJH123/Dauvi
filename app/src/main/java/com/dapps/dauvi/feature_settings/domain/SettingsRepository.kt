package com.dapps.dauvi.feature_settings.domain

import com.dapps.dauvi.core.util.LocationDs
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val agreedTermsFlow: Flow<Boolean>
    val locationFlow: Flow<LocationDs?>
    val highUviValueFlow: Flow<Int?>
    val dailyUviAlertOnFlow: Flow<Boolean?>
    val dailyUviAlertTimeValueFlow: Flow<Long?>
    val highUviAlertOnFlow: Flow<Boolean?>
    val highUviAlertValueFlow: Flow<Int?>

    suspend fun updateAgreedTerms(agreed: Boolean)
    suspend fun updateLocation(locationDs: LocationDs)
    suspend fun setHighUviValue(highUvi: Int)
    suspend fun setDailyUviAlertOn(dailyUviAlertOn: Boolean)
    suspend fun setDailyUviAlertTimeValue(dailyUviAlertTimeValue: Long)
    suspend fun setHighUviAlertOn(highUviAlertOn: Boolean)
    suspend fun setHighUviAlertValue(highUviAlertValue: Int)
}