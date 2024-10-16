package com.dapps.dauvi.feature_uvi.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dapps.dauvi.feature_uvi.data.db.WeatherDatabase

@Entity(tableName = WeatherDatabase.CURRENT_WEATHER_TABLE_NAME)
class DbCurrentWeather (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(WeatherDatabase.CURRENT_WEATHER_PK_COLUMN_NAME)
    val id: Int = 0,
    val timeOfRequest: Long,
    val lastUpdated: Long,
    val uvi: Double,
    val sunrise: Long,
    val sunset: Long,
    @Embedded val location: DbWeatherLocation,
)

