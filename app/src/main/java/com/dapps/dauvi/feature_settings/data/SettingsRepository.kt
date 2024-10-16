package com.dapps.dauvi.feature_settings.data

import com.dapps.dauvi.core.util.LocationDs
import com.dapps.dauvi.feature_settings.data.datastore.IDataStoreManager
import com.dapps.dauvi.feature_settings.domain.SettingsRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val dataStoreManager: IDataStoreManager
): SettingsRepository {

    override val agreedTermsFlow: Flow<Boolean> = dataStoreManager.hasAgreedTermsFlow
    override val locationFlow = dataStoreManager.locationFlow
    override val highUviValueFlow = dataStoreManager.highUviValueFlow
    override val dailyUviAlertOnFlow = dataStoreManager.dailyUviAlertOnFlow
    override val dailyUviAlertTimeValueFlow = dataStoreManager.dailyUviAlertTimeValueFlow
    override val highUviAlertOnFlow = dataStoreManager.highUviAlertOnFlow
    override val highUviAlertValueFlow = dataStoreManager.highUviAlertValueFlow

    override suspend fun updateAgreedTerms(agreed: Boolean) =
        dataStoreManager.setAgreedTerms(agreed)
    override suspend fun updateLocation(locationDs: LocationDs) =
        dataStoreManager.updateLocation(locationDs)
    override suspend fun setHighUviValue(highUvi: Int) =
        dataStoreManager.setHighUviValue(highUvi)
    override suspend fun setDailyUviAlertOn(dailyUviAlertOn: Boolean) =
        dataStoreManager.setDailyUviAlertOn(dailyUviAlertOn)
    override suspend fun setDailyUviAlertTimeValue(dailyUviAlertTimeValue: Long) =
        dataStoreManager.setDailyUviAlertTimeValue(dailyUviAlertTimeValue)
    override suspend fun setHighUviAlertOn(highUviAlertOn: Boolean) =
        dataStoreManager.setHighUviAlertOn(highUviAlertOn)
    override suspend fun setHighUviAlertValue(highUviAlertValue: Int) =
        dataStoreManager.setHighUviAlertValue(highUviAlertValue)


}