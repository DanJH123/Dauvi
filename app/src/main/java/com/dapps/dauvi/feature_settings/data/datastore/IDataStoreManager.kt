package com.dapps.dauvi.feature_settings.data.datastore

import com.dapps.dauvi.core.util.LocationDs
import kotlinx.coroutines.flow.Flow

interface IDataStoreManager {
    val hasAgreedTermsFlow: Flow<Boolean>
    val locationFlow: Flow<LocationDs?>

    val highUviValueFlow: Flow<Int?>

    val dailyUviAlertOnFlow: Flow<Boolean?>
    val dailyUviAlertTimeValueFlow: Flow<Long?>

    val highUviAlertOnFlow: Flow<Boolean?>
    val highUviAlertValueFlow: Flow<Int?>

    suspend fun setAgreedTerms(agreed: Boolean)
    suspend fun updateLocation(locationDs: LocationDs)

    suspend fun deleteAgreedTerms()
    suspend fun deleteLocation()

    suspend fun setHighUviValue(highUvi: Int)
    suspend fun deleteHighUviValue()

    suspend fun setDailyUviAlertOn(dailyUviAlertOn: Boolean)
    suspend fun deleteDailyUviAlertOn()

    suspend fun setDailyUviAlertTimeValue(dailyUviAlertTimeValue: Long)
    suspend fun deleteDailyUviAlertTimeValue()

    suspend fun setHighUviAlertOn(highUviAlertOn: Boolean)
    suspend fun deleteHighUviAlertOn()

    suspend fun setHighUviAlertValue(highUviAlertValue: Int)
    suspend fun deleteHighUviAlertValue()

    suspend fun deleteAll()
}