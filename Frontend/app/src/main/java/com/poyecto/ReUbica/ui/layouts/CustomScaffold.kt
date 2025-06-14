package com.poyecto.ReUbica.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.poyecto.ReUbica.ui.screens.FavoriteScreen
import com.poyecto.ReUbica.ui.screens.HomeScreen
import com.poyecto.ReUbica.ui.screens.LegalInformationScreen
import com.poyecto.ReUbica.ui.screens.NotificationsScreen
import com.poyecto.ReUbica.ui.screens.PersonalInformationScreen
import com.poyecto.ReUbica.ui.screens.PoliticaDePrivacidad
import com.poyecto.ReUbica.ui.screens.ProfileScreen
import com.poyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocal
import com.poyecto.ReUbica.ui.screens.SearchScreen
import com.poyecto.ReUbica.ui.screens.TerminosYcondiciones
import com.poyecto.ReUbica.ui.viewmodel.FavoritosViewModel
import com.proyecto.ReUbica.ui.navigations.FavoritesScreenNavigation
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LegalInformationNavigation
import com.proyecto.ReUbica.ui.navigations.NotificationsNavigation
import com.proyecto.ReUbica.ui.navigations.PersonalDataNavigation
import com.proyecto.ReUbica.ui.navigations.PoliticaDePrivacidadNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalNavigation
import com.proyecto.ReUbica.ui.navigations.SearchScreenNavigation
import com.proyecto.ReUbica.ui.navigations.TerminosYCondicionesNavigation

data class navItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun CustomScaffold(){

    val favoritosViewModel: FavoritosViewModel = viewModel()
    val navController = rememberNavController()
    var title by rememberSaveable { mutableStateOf("Home") }
    var selectedItem by rememberSaveable { mutableStateOf("nowplaying") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        navItem("Inicio", Icons.Filled.Home, "nowplaying"),
        navItem("Buscar", Icons.Default.Search, "search"),
        navItem("Mi cuenta", Icons.Filled.AccountCircle, "account"),
        navItem("Favoritos", Icons.Default.Favorite, "favorites")
    )

    fun onItemSelected(currentItem: String) {
        selectedItem = currentItem
        title = when (currentItem) {
            "nowplaying" -> "Inicio"
            "search" -> "Buscar"
            "account" -> "Mi cuenta"
            "favorites" -> "Favoritos"
            else -> "Inicio"
        }
        when (currentItem) {
            "nowplaying" -> navController.navigate(HomeScreenNavigation)
            "search" -> navController.navigate(SearchScreenNavigation)
            "account" -> navController.navigate(ProfileScreenNavigation)
            "favorites" -> navController.navigate(FavoritesScreenNavigation)
            else -> navController.navigate(HomeScreenNavigation)
        }
    }


    Scaffold (
        topBar = {
            if (currentRoute != ProfileScreenNavigation::class.qualifiedName &&
                currentRoute !=  PersonalDataNavigation::class.qualifiedName){
                TopBar(navController)
            }
        },
            bottomBar = { BottomBar ( navItems = navItems,
            selectedItem = selectedItem,
            onItemSelected = { onItemSelected(it) }) },
        containerColor = Color.White
    ) { innerPadding ->
        Column {
            NavHost(
                navController = navController,
                startDestination = HomeScreenNavigation,
                Modifier.padding(innerPadding)

            ) {
                composable<HomeScreenNavigation> {
                    HomeScreen(navController = navController, favoritosViewModel = favoritosViewModel)
                }

                composable<FavoritesScreenNavigation> {
                    FavoriteScreen(favoritosViewModel = favoritosViewModel)
                }


                composable<SearchScreenNavigation>{
                    SearchScreen(navController)
                }

                composable<ProfileScreenNavigation>{
                    ProfileScreen(navController)
                }

                composable<PersonalDataNavigation> {
                    PersonalInformationScreen(navController)
                }

                composable<RegisterLocalNavigation> {
                    RegisterLocal(navController)
                }

                composable<NotificationsNavigation> {
                    NotificationsScreen(navController)
                }

                composable<LegalInformationNavigation> {
                    LegalInformationScreen(navController)
                }

                composable <TerminosYCondicionesNavigation> {
                    TerminosYcondiciones(navController)
                }

                composable <PoliticaDePrivacidadNavigation> {
                    PoliticaDePrivacidad(navController)
                }


            }
            Spacer(modifier = Modifier.padding(innerPadding))
        }
    }
}