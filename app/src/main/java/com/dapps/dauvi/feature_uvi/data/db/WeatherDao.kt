package com.dapps.dauvi.feature_uvi.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dapps.dauvi.feature_uvi.data.db.model.DbCurrentWeather
import com.dapps.dauvi.feature_uvi.data.db.model.DbCurrentWeatherWithHourlyForecast
import com.dapps.dauvi.feature_uvi.data.db.model.DbHourlyForecastItem
import com.dapps.dauvi.feature_uvi.data.db.model.copy
import kotlinx.coroutines.flow.Flow


@Dao
abstract class WeatherDao {

    /** GET */

    @Transaction
    @Query("SELECT * " +
            "FROM ${WeatherDatabase.CURRENT_WEATHER_TABLE_NAME} " +
            "ORDER BY ${WeatherDatabase.CURRENT_WEATHER_TIME_COLUMN_NAME} DESC " +
            "LIMIT 1")
    abstract fun getLatestCurrentWeatherWithHourlyForecast(): Flow<DbCurrentWeatherWithHourlyForecast?>


    @Query("SELECT * " +
            "FROM ${WeatherDatabase.CURRENT_WEATHER_TABLE_NAME} " +
            "WHERE id = :id " +
            "LIMIT 1")
    abstract suspend fun getCurrentWeatherById(id: Int): DbCurrentWeather

    /** INSERT */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCurrentWeather(currentWeather: DbCurrentWeather): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertHourlyForecastItems(hourlyForecastItems: List<DbHourlyForecastItem>): List<Long>

    /** DELETE */

    @Query("DELETE FROM ${WeatherDatabase.HOURLY_FORECAST_TABLE_NAME}")
    abstract suspend fun deleteAllHourlyForecastItems()

    @Query("DELETE FROM ${WeatherDatabase.CURRENT_WEATHER_TABLE_NAME}")
    abstract suspend fun deleteAllCurrentWeather()

    @Transaction
    open suspend fun deleteAll() {
        deleteAllHourlyForecastItems()
        deleteAllCurrentWeather()
    }

    @Transaction
    open suspend fun updateWeatherAndForecast(
        dbWeather: DbCurrentWeather,
        dbHourlyForecastItems: List<DbHourlyForecastItem>,
    ) {
        // Clear existing items
        deleteAll()

        // Insert new current weather and get the auto-generated id
        val storedId = insertCurrentWeather(dbWeather).toInt()

        // Provide the auto-generated id to the hourly forecast items
        val dbHourlyForecastItemsWithIds = dbHourlyForecastItems.map {
            item -> item.copy(currentWeatherId = storedId)
        }

        // Insert hourly forecast items
        insertHourlyForecastItems(dbHourlyForecastItemsWithIds)
    }
}