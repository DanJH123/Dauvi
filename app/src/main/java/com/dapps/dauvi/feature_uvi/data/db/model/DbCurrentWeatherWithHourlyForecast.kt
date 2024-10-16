package com.dapps.dauvi.feature_uvi.data.db.model

import androidx.room.Embedded
import androidx.room.Relation
import com.dapps.dauvi.feature_uvi.data.db.WeatherDatabase

data class DbCurrentWeatherWithHourlyForecast(
    @Embedded val currentWeather: DbCurrentWeather,
    @Relation(
        parentColumn = WeatherDatabase.CURRENT_WEATHER_PK_COLUMN_NAME,
        entityColumn = WeatherDatabase.HOURLY_FORECAST_CURRENT_WEATHER_COLUMN_NAME,
    ) val hourlyForecastItems: List<DbHourlyForecastItem>
)