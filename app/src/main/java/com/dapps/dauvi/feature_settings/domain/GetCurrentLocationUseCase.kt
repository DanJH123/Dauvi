package com.dapps.dauvi.feature_settings.domain

import android.location.Geocoder
import androidx.annotation.RequiresPermission
import com.dapps.dauvi.core.util.LocationDs
import com.dapps.dauvi.core.util.geocode.getFromLocationSuspend
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GetCurrentLocationUseCase(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
) {
    @RequiresPermission("android.permission.ACCESS_COARSE_LOCATION")
    suspend operator fun invoke(): LocationDs? {
        val location = try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (location == null) { return null }

        return try {
            withContext(Dispatchers.IO) {
                val addresses = geocoder.getFromLocationSuspend(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    maxResults = 1
                )

                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    LocationDs(
                        city = address.locality ?: address.subLocality ?: address.subAdminArea,
                        country = address.countryName ?: address.adminArea,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
