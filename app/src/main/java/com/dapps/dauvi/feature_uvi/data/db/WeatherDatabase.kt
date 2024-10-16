package com.dapps.dauvi.feature_uvi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dapps.dauvi.feature_uvi.data.db.model.DbCurrentWeather
import com.dapps.dauvi.feature_uvi.data.db.model.DbHourlyForecastItem

@Database(
    entities = [DbCurrentWeather::class, DbHourlyForecastItem::class, ],
    version = 1,
    exportSchema = false,
)
abstract class WeatherDatabase: RoomDatabase() {

    abstract val weatherDao: WeatherDao

    companion object {
        const val DATABASE_NAME = "weather_db"
        const val CURRENT_WEATHER_TABLE_NAME: String = "current_weather"
        const val CURRENT_WEATHER_PK_COLUMN_NAME: String = "id"
        const val CURRENT_WEATHER_TIME_COLUMN_NAME: String = "timeOfRequest"

        const val HOURLY_FORECAST_TABLE_NAME: String = "hourly_forecast"
        const val HOURLY_FORECAST_CURRENT_WEATHER_COLUMN_NAME: String = "current_weather_id"
    }
}