package com.dapps.dauvi.feature_uvi.data.network

interface IWeatherDataSource {
    suspend fun getWeatherForecast(
        days: Int,
        latitude: Double,
        longitude: Double,
    ): NetworkWeatherForecastResponse
}