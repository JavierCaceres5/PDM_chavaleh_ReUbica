package com.proyecto.ReUbica.ui.navigations

import kotlinx.serialization.Serializable

@Serializable
object WelcomeScreenNavigation

@Serializable
object RegistroNavigation

@Serializable
object LoginScreenNavigation

@Serializable
object HomeScreenNavigation

@Serializable
object SearchScreenNavigation

@Serializable
object ProfileScreenNavigation

@Serializable
object FavoritesScreenNavigation

@Serializable
object mainNavigation

@Serializable
object PersonalDataNavigation

@Serializable
object NotificationsNavigation

@Serializable
object RegisterLocalNavigation

@Serializable
object LegalInformationNavigation

@Serializable
object TerminosYCondicionesNavigation

@Serializable
object PoliticaDePrivacidadNavigation

@Serializable
object RegisterLocalScreen1Navigation

@Serializable
object RegisterLocalScreen2Navigation

@Serializable
object RegisterLocalScreen3Navigation

@Serializable
object SessionCheckNavigation

@Serializable
object LoadingScreenNavigation

@Serializable
object CartaProductosScreenNavigation

@Serializable
data class ComercioNavigation(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val horario: String
)
object ProductDetailNavigation {
    const val route = "product_detail"
    const val tokenArg = "token"
    const val emprendimientoIdArg = "emprendimientoID"

    fun withArgs(productId: String, token: String, emprendimientoID: String): String {
        return "$route/$productId?$tokenArg=$token&$emprendimientoIdArg=$emprendimientoID"
    }
}

@Serializable
object LocalInformationScreenNavigation
