package com.dapps.dauvi.core.util.geocode

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Geocoder.getFromLocationSuspend(
    latitude: Double,
    longitude: Double,
    maxResults: Int = 1
): List<Address> = suspendCancellableCoroutine { c ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getFromLocation(latitude, longitude, maxResults) { addresses ->
            if (addresses.isNotEmpty()) {
                c.resume(addresses)
            } else {
                c.resumeWithException(IOException("No addresses found"))
            }
        }
    } else {
        try {
            val addresses = this.getFromLocation(latitude, longitude, maxResults)
            if (!addresses.isNullOrEmpty()) {
                c.resume(addresses)
            } else {
                c.resumeWithException(IOException("No addresses found"))
            }
        } catch (e: IOException) {
            c.resumeWithException(e)
        }
    }
}