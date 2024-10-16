package com.dapps.dauvi.feature_uvi.data.db.model

import androidx.room.ColumnInfo

data class DbWeatherLocation(
    @ColumnInfo(name = "location_name")
    val name: String,
    @ColumnInfo(name = "location_region")
    val region: String,
    @ColumnInfo(name = "location_country")
    val country: String,
    @ColumnInfo(name = "location_latitude")
    val latitude: Double,
    @ColumnInfo(name = "location_longitude")
    val longitude: Double,
    @ColumnInfo(name = "location_timezone_id")
    val timezoneId: String,
)