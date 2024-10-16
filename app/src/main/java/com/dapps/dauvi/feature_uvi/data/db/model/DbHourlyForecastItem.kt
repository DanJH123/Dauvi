package com.dapps.dauvi.feature_uvi.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dapps.dauvi.feature_uvi.data.db.WeatherDatabase

@Entity(
    tableName = WeatherDatabase.HOURLY_FORECAST_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = DbCurrentWeather::class,
            childColumns = [WeatherDatabase.HOURLY_FORECAST_CURRENT_WEATHER_COLUMN_NAME],
            parentColumns = [WeatherDatabase.CURRENT_WEATHER_PK_COLUMN_NAME]
        )]
    )
class DbHourlyForecastItem (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = WeatherDatabase.HOURLY_FORECAST_CURRENT_WEATHER_COLUMN_NAME, index = true)
    val currentWeatherId: Int,
    val timestamp: Long,
    val uvi: Double,
)
fun DbHourlyForecastItem.copy(
    id: Int = this.id,
    currentWeatherId: Int = this.currentWeatherId,
    timestamp: Long = this.timestamp,
    uvi: Double = this.uvi,
): DbHourlyForecastItem {
    return DbHourlyForecastItem(
        id = id,
        currentWeatherId = currentWeatherId,
        timestamp = timestamp,
        uvi = uvi,
    )
}



