package com.dapps.dauvi.feature_uvi.domain.model

import com.dapps.dauvi.feature_uvi.data.db.model.DbWeatherLocation


data class UviLocation(
    val name: String,
    val region: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String,
)

fun DbWeatherLocation.toUviLocation(): UviLocation {
    return UviLocation(
        name = name,
        region = region,
        country = country,
        latitude = latitude,
        longitude = longitude,
        timezoneId = timezoneId
    )
}

