package com.dapps.dauvi.feature_uvi.data.network

import com.squareup.moshi.Json

class NetworkWeatherForecastResponse(
    @Json(name = "location") val location: NetworkWeatherLocation,
    @Json(name = "current") val current: NetworkCurrentWeather,
    @Json(name = "forecast") val forecast: NetworkWeatherForecast,
)

class NetworkWeatherLocation(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "tz_id") val timezoneId: String,
)

class NetworkCurrentWeather(
    @Json(name = "last_updated_epoch")val lastUpdated: Long,
    @Json(name = "uv") val uv: Double,
)

class NetworkWeatherForecast(
    @Json(name = "forecastday") val forecastDays: List<NetworkWeatherForecastDay>
)

class NetworkWeatherForecastDay(
    @Json(name = "hour") val hours: List<NetworkWeatherForecastHour>,
    @Json(name = "astro") val astro: NetworkWeatherForecastAstro
)

class NetworkWeatherForecastHour(
    @Json(name = "time_epoch") val time: Long,
    @Json(name = "uv") val uv: Double,
)

class NetworkWeatherForecastAstro(
    @Json(name = "sunrise") val sunrise: String,
    @Json(name = "sunset") val sunset: String,
)