package com.poyecto.ReUbica.data

import com.google.android.gms.maps.model.LatLng

data class Comercio(
    val name: String,
    val description: String,
    val hours: String,
    val locationName: String,
    val location: LatLng,
    val products: List<Producto>
)
