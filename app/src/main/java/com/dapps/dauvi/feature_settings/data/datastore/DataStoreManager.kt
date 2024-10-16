package com.dapps.dauvi.feature_settings.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dapps.dauvi.core.util.LocationDs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor (@ApplicationContext private val context: Context)
    : IDataStoreManager {

    companion object {
        private val KEY_HAS_AGREED_TERMS = booleanPreferencesKey("has_agreed_terms")
        private val KEY_LATITUDE = doublePreferencesKey("latitude")
        private val KEY_LONGITUDE = doublePreferencesKey("longitude")
        private val KEY_CITY = stringPreferencesKey("city")
        private val KEY_COUNTRY = stringPreferencesKey("country")
        private val KEY_HIGH_UVI = intPreferencesKey("high_uvi")
        private val KEY_DAILY_UVI_ALERT_ON = booleanPreferencesKey("daily_uvi_alert_on")
        private val KEY_DAILY_UVI_ALERT_VALUE = longPreferencesKey("daily_uvi_alert_value")
        private val KEY_HIGH_UVI_ALERT_ON = booleanPreferencesKey("high_uvi_alert_on")
        private val KEY_HIGH_UVI_ALERT_VALUE = intPreferencesKey("high_uvi_alert_value")

        private const val DATA_STORE_FILE = "app_preferences"
    }

    private val Context.dataStore:
            DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_FILE)

    override val hasAgreedTermsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_HAS_AGREED_TERMS] ?: false
    }

    override val locationFlow: Flow<LocationDs?> = context.dataStore.data.map { preferences ->
        preferences[KEY_CITY]?.let { city ->
            preferences[KEY_COUNTRY]?.let { country ->
                preferences[KEY_LATITUDE]?.let { latitude ->
                    preferences[KEY_LONGITUDE]?.let { longitude ->
                        LocationDs(city, country, latitude, longitude)
                    }
                }
            }
        }
    }

    override val highUviValueFlow: Flow<Int?> =
        context.dataStore.data.map { it[KEY_HIGH_UVI] }
    override val dailyUviAlertOnFlow: Flow<Boolean?> =
        context.dataStore.data.map { it[KEY_DAILY_UVI_ALERT_ON] }
    override val dailyUviAlertTimeValueFlow: Flow<Long?> =
        context.dataStore.data.map { it[KEY_DAILY_UVI_ALERT_VALUE] }
    override val highUviAlertOnFlow: Flow<Boolean?> =
        context.dataStore.data.map { it[KEY_HIGH_UVI_ALERT_ON] }
    override val highUviAlertValueFlow: Flow<Int?> =
        context.dataStore.data.map { it[KEY_HIGH_UVI_ALERT_VALUE] }


    override suspend fun setAgreedTerms(agreed: Boolean) {
        context.dataStore.edit { it[KEY_HAS_AGREED_TERMS] = agreed }
    }

    override suspend fun deleteAgreedTerms() {
        context.dataStore.edit { it.remove(KEY_HAS_AGREED_TERMS) }
    }

    override suspend fun updateLocation(locationDs: LocationDs) {
        context.dataStore.edit {
            it[KEY_CITY] = locationDs.city ?: ""
            it[KEY_COUNTRY] = locationDs.country ?: ""
            it[KEY_LATITUDE] = locationDs.latitude
            it[KEY_LONGITUDE] = locationDs.longitude
        }
    }

    override suspend fun deleteLocation() {
        context.dataStore.edit {
            it.remove(KEY_CITY)
            it.remove(KEY_COUNTRY)
            it.remove(KEY_LATITUDE)
            it.remove(KEY_LONGITUDE)
        }
    }

    override suspend fun setHighUviValue(highUvi: Int) {
        context.dataStore.edit { it[KEY_HIGH_UVI] = highUvi }
    }
    override suspend fun deleteHighUviValue(){
        context.dataStore.edit { it.remove(KEY_HIGH_UVI) }
    }

    override suspend fun setDailyUviAlertOn(dailyUviAlertOn: Boolean) {
        context.dataStore.edit { it[KEY_DAILY_UVI_ALERT_ON] = dailyUviAlertOn }
    }
    override suspend fun deleteDailyUviAlertOn(){
        context.dataStore.edit { it.remove(KEY_DAILY_UVI_ALERT_ON) }
    }

    override suspend fun setDailyUviAlertTimeValue(dailyUviAlertTimeValue: Long) {
        context.dataStore.edit { it[KEY_DAILY_UVI_ALERT_VALUE] = dailyUviAlertTimeValue }
    }
    override suspend fun deleteDailyUviAlertTimeValue(){
        context.dataStore.edit { it.remove(KEY_DAILY_UVI_ALERT_VALUE) }
    }

    override suspend fun setHighUviAlertOn(highUviAlertOn: Boolean) {
        context.dataStore.edit { it[KEY_HIGH_UVI_ALERT_ON] = highUviAlertOn }
    }
    override suspend fun deleteHighUviAlertOn(){
        context.dataStore.edit { it.remove(KEY_HIGH_UVI_ALERT_ON) }
    }

    override suspend fun setHighUviAlertValue(highUviAlertValue: Int) {
        context.dataStore.edit { it[KEY_HIGH_UVI_ALERT_VALUE] = highUviAlertValue }
    }
    override suspend fun deleteHighUviAlertValue(){
        context.dataStore.edit { it.remove(KEY_HIGH_UVI_ALERT_VALUE) }
    }

    override suspend fun deleteAll() {
        context.dataStore.edit { it.clear() }
    }
}