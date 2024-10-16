package com.dapps.dauvi.feature_uvi.domain.model.ext

import com.dapps.dauvi.feature_uvi.data.db.model.DbCurrentWeather
import com.dapps.dauvi.feature_uvi.data.db.model.DbCurrentWeatherWithHourlyForecast
import com.dapps.dauvi.feature_uvi.data.db.model.DbHourlyForecastItem
import com.dapps.dauvi.feature_uvi.data.db.model.DbWeatherLocation
import com.dapps.dauvi.feature_uvi.data.network.NetworkWeatherForecastResponse
import com.dapps.dauvi.feature_uvi.domain.model.UviForecastItem
import com.dapps.dauvi.feature_uvi.domain.model.Uvi
import com.dapps.dauvi.feature_uvi.domain.model.toUviLocation

fun DbCurrentWeatherWithHourlyForecast.toDomain(): Uvi {
    return Uvi(
        id = currentWeather.id,
        uvi = currentWeather.uvi,
        hourlyForecast = hourlyForecastItems.map { it.toDomain() },
        timeOfRequest = currentWeather.timeOfRequest,
        lastUpdated = currentWeather.lastUpdated,
        location = currentWeather.location.toUviLocation(),
        sunrise = currentWeather.sunrise,
        sunset = currentWeather.sunset
    )
}

fun NetworkWeatherForecastResponse.toDbCurrentWeather(
    timeOfRequest: Long,
    sunrise: Long,
    sunset: Long,
): DbCurrentWeather {
    val dbWeatherLocation = DbWeatherLocation(
        name = this.location.name,
        region = this.location.region,
        country = this.location.country,
        latitude = this.location.lat,
        longitude = this.location.lon,
        timezoneId = this.location.timezoneId,
    )

    return DbCurrentWeather(
        timeOfRequest = timeOfRequest,
        lastUpdated = this.current.lastUpdated,
        uvi = this.current.uv,
        sunrise = sunrise,
        sunset = sunset,
        location = dbWeatherLocation
    )
}

fun NetworkWeatherForecastResponse.toDbHourlyForecastItems(currentWeatherId: Int): List<DbHourlyForecastItem> {
    return forecast.forecastDays.flatMap { forecastDay ->
        forecastDay.hours.map { hour ->
            DbHourlyForecastItem(
                currentWeatherId = currentWeatherId,
                timestamp = hour.time,
                uvi = hour.uv
            )}
        }
}


fun DbHourlyForecastItem.toDomain(): UviForecastItem {
    return UviForecastItem(
        epochTimeSeconds = timestamp,
        uvi = uvi
    )
}