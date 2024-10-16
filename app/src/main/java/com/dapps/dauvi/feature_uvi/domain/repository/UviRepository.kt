package com.dapps.dauvi.feature_uvi.domain.repository

import com.dapps.dauvi.core.util.currentTimeSeconds
import com.dapps.dauvi.core.util.timeStringToLongSeconds
import com.dapps.dauvi.feature_uvi.data.db.WeatherDao
import com.dapps.dauvi.feature_uvi.data.network.IWeatherDataSource
import com.dapps.dauvi.feature_uvi.domain.model.Uvi
import com.dapps.dauvi.feature_uvi.domain.model.ext.toDbCurrentWeather
import com.dapps.dauvi.feature_uvi.domain.model.ext.toDbHourlyForecastItems
import com.dapps.dauvi.feature_uvi.domain.model.ext.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val WEATHER_API_DAYS = 1

@Singleton
class UviRepository @Inject constructor (
    private val dataSource: IWeatherDataSource,
    private val weatherDao: WeatherDao
) : IUviRepository {

    override val currentUviFlow: Flow<Uvi?> =
        weatherDao.getLatestCurrentWeatherWithHourlyForecast()
        .map { item -> item?.toDomain() }

    /** Load data from data source and store in database */
    override suspend fun loadUviDataForLocation(latitude: Double, longitude: Double) {
        val networkWeather = dataSource.getWeatherForecast(
            latitude = latitude,
            longitude = longitude,
            days = WEATHER_API_DAYS
        )

        val networkAstro = networkWeather.forecast.forecastDays.first().astro

        val dbWeather = networkWeather.toDbCurrentWeather(
            currentTimeSeconds(),
            sunrise = timeStringToLongSeconds(
                networkAstro.sunrise,
                timePattern = "hh:mm a",
                timezoneId = networkWeather.location.timezoneId
            ),
            sunset = timeStringToLongSeconds(
                networkAstro.sunset,
                timePattern = "hh:mm a",
                timezoneId = networkWeather.location.timezoneId
            ),
        )

        // Current weatherID will be set after auto-generation in updateWeatherAndForecast() below
        val dbHourlyForecastItems = networkWeather.toDbHourlyForecastItems(0)
        weatherDao.updateWeatherAndForecast(dbWeather, dbHourlyForecastItems)
    }

    override suspend fun clearUviData() {
        weatherDao.deleteAll()
    }
}