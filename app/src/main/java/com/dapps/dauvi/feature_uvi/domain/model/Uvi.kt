package com.dapps.dauvi.feature_uvi.domain.model

data class Uvi(
    val id: Int,
    val uvi: Double,
    val hourlyForecast: List<UviForecastItem>,
    val timeOfRequest: Long,
    val lastUpdated: Long,
    val location: UviLocation,
    val sunrise: Long?,
    val sunset: Long?,
)

