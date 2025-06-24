package com.poyecto.ReUbica.data

import com.google.android.gms.maps.model.LatLng

val dummyBusiness = Comercio(
    name = "Solemare",
    description = "Diseños únicos, hechos a mano con amor...",
    hours = "Horario 2–5 pm",
    locationName = "Universidad Jose Simeon Cañas",
    location = LatLng(13.6822, -89.2396), // ubicación dummy
    products = listOf(
        Producto(1, "Collar Solemare", "Collar dorado con dije...", 5.99, 4.5, "url1"),
        Producto(2, "Anillo artesanal", "Anillo hecho a mano...", 5.99, 4.5, "url2"),
        Producto(3, "Aretes Solemare", "Par de aretes tejidos...", 5.99, 4.5, "url3"),
        Producto(4, "Pulsera con dijes", "Pulsera artesanal dorada...", 5.99, 4.5, "url4")
    )
)

