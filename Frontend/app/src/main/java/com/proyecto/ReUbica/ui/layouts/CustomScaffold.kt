package com.proyecto.ReUbica.ui.layouts

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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoriteScreen
import com.proyecto.ReUbica.ui.screens.HomeScreen
import com.proyecto.ReUbica.ui.screens.LegalInformationScreen
import com.proyecto.ReUbica.ui.screens.NotificationsScreen
import com.proyecto.ReUbica.ui.screens.PersonalInformationScreen.PersonalInformationScreen
import com.proyecto.ReUbica.ui.screens.PoliticaDePrivacidad
import com.proyecto.ReUbica.ui.screens.ProfileScreen.ProfileScreen
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocal
import com.proyecto.ReUbica.ui.screens.SearchScreen
import com.proyecto.ReUbica.ui.screens.TerminosYcondiciones
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.ui.navigations.FavoritesScreenNavigation
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LegalInformationNavigation
import com.proyecto.ReUbica.ui.navigations.NotificationsNavigation
import com.proyecto.ReUbica.ui.navigations.PersonalDataNavigation
import com.proyecto.ReUbica.ui.navigations.PoliticaDePrivacidadNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen1Navigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen2Navigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen3Navigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen4Navigation
import com.proyecto.ReUbica.ui.navigations.SearchScreenNavigation
import com.proyecto.ReUbica.ui.navigations.TerminosYCondicionesNavigation
import com.proyecto.ReUbica.ui.screens.PersonalInformationScreen.PersonalInformationViewModel
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocalScreen1
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocalScreen2
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocalScreen3
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocalScreen4
import com.proyecto.ReUbica.ui.screens.RegisterScreen.RegisterScreenViewModel

data class navItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun CustomScaffold(rootNavController: NavHostController){

    val navController = rememberNavController()
    var title by rememberSaveable { mutableStateOf("Home") }
    var selectedItem by rememberSaveable { mutableStateOf("nowplaying") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val favoritosViewModel: FavoritosViewModel = viewModel()


    val navItems = listOf(
        navItem("Inicio", Icons.Filled.Home, "nowplaying"),
        navItem("Buscar", Icons.Default.Search, "search"),
        navItem("Favoritos", Icons.Default.Favorite, "favorites"),
        navItem("Mi cuenta", Icons.Filled.AccountCircle, "account")
    )

    fun onItemSelected(currentItem: String) {
        selectedItem = currentItem
        title = when (currentItem) {
            "nowplaying" -> "Inicio"
            "search" -> "Buscar"
            "favorites" -> "Favoritos"
            "account" -> "Mi cuenta"
            else -> "Inicio"
        }
        when (currentItem) {
            "nowplaying" -> navController.navigate(HomeScreenNavigation)
            "search" -> navController.navigate(SearchScreenNavigation)
            "favorites" -> navController.navigate(FavoritesScreenNavigation)
            "account" -> navController.navigate(ProfileScreenNavigation)
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
                    SearchScreen(favoritosViewModel = favoritosViewModel)
                }

                composable<ProfileScreenNavigation>{
                    ProfileScreen(navController = navController, rootNavController = rootNavController)
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
                composable<RegisterLocalScreen1Navigation> {
                    RegisterLocalScreen1(navController)
                }

                composable<RegisterLocalScreen2Navigation> {
                    RegisterLocalScreen2(navController)
                }

                composable<RegisterLocalScreen3Navigation> {
                    RegisterLocalScreen3(navController)
                }

                composable<RegisterLocalScreen4Navigation> {
                    RegisterLocalScreen4(navController)
                }

            }
            Spacer(modifier = Modifier.padding(innerPadding))
        }
    }
}