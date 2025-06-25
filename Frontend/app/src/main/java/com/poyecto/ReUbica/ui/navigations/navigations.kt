package com.proyecto.ReUbica.ui.navigations

import kotlinx.serialization.Serializable

const val MainNavigationRoute = "mainNavigation"


@Serializable
object WelcomeScreenNavigation

@Serializable
object RegistroNavigation

@Serializable
object LoginScreenNavigation

@Serializable
object HomeScreenNavigation {
    const val route = "nowplaying"
}

@Serializable
object SearchScreenNavigation {
    const val route = "search"
}

@Serializable
object ProfileScreenNavigation {
    const val route = "account"
}

@Serializable
object FavoritesScreenNavigation {
    const val route = "favorites"
}

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
object DetallesComercioNavigation

