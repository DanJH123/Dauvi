package com.dapps.dauvi.feature_uvi.data.network

import com.dapps.dauvi.BuildConfig
import com.dapps.dauvi.core.util.AuthParamInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

private interface RetrofitWeatherApi {

    @GET(value = "forecast.json")
    suspend fun getWeatherForecastByQ(
        @Query("days") forecastDays: Int,
        @Query("q") q: String,
    ): NetworkWeatherForecastResponse

}

private const val WEATHER_API_BASE_URL = "https://api.weatherapi.com/v1/"

@Singleton
class WeatherApiDataSource : IWeatherDataSource {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthParamInterceptor(BuildConfig.WEATHER_API_KEY))
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        })
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val networkApi =
        Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .client(okHttpClient)
            .callFactory{ okHttpClient.newCall(it) }
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RetrofitWeatherApi::class.java)


    override suspend fun getWeatherForecast(
        days: Int,
        latitude: Double,
        longitude: Double
    ): NetworkWeatherForecastResponse {
        val result = networkApi.getWeatherForecastByQ(days, "$latitude,$longitude")
        return result
    }

}