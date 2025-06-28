package com.proyecto.ReUbica.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object LocationUtils {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Location? {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { cont ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    cont.resume(location)
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }

    fun calculateDistance(
        userLat: Double,
        userLon: Double,
        placeLat: Double,
        placeLon: Double
    ): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(placeLat - userLat)
        val dLon = Math.toRadians(placeLon - userLon)
        val lat1 = Math.toRadians(userLat)
        val lat2 = Math.toRadians(placeLat)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadiusKm * c
    }
}
