package com.proyecto.ReUbica.ui.layouts

import android.R.attr.type
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.google.common.base.Defaults.defaultValue
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.data.model.emprendimiento.toEmprendimientoModel
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import com.proyecto.ReUbica.ui.navigations.CartaProductosScreenNavigation
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoriteScreen
import com.proyecto.ReUbica.ui.screens.HomeScreen.HomeScreen
import com.proyecto.ReUbica.ui.screens.LegalInformationScreen
import com.proyecto.ReUbica.ui.screens.NotificationsScreen
import com.proyecto.ReUbica.ui.screens.PersonalInformationScreen.PersonalInformationScreen
import com.proyecto.ReUbica.ui.screens.PoliticaDePrivacidad
import com.proyecto.ReUbica.ui.screens.ProfileScreen.ProfileScreen
import com.proyecto.ReUbica.ui.screens.RegisterLocal
import com.proyecto.ReUbica.ui.screens.SearchScreen.SearchScreen
import com.proyecto.ReUbica.ui.screens.TerminosYcondiciones
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.ui.navigations.FavoritesScreenNavigation
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LegalInformationNavigation
import com.proyecto.ReUbica.ui.navigations.LoadingScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LocalInformationScreenNavigation
import com.proyecto.ReUbica.ui.navigations.NotificationsNavigation
import com.proyecto.ReUbica.ui.navigations.PersonalDataNavigation
import com.proyecto.ReUbica.ui.navigations.PoliticaDePrivacidadNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen1Navigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen2Navigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalScreen3Navigation
import com.proyecto.ReUbica.ui.navigations.SearchScreenNavigation
import com.proyecto.ReUbica.ui.navigations.TerminosYCondicionesNavigation
import com.proyecto.ReUbica.ui.screens.CartaProductos.CartaProductosScreen
import com.proyecto.ReUbica.ui.screens.CartaProductos.CartaProductosViewModel
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocalScreen1
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegisterLocalScreen2
import com.proyecto.ReUbica.ui.screens.ComercioScreen.ComercioScreen
import com.proyecto.ReUbica.ui.screens.ProductoScreen.ProductDetailScreen
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegistroComercioViewModel
import com.proyecto.ReUbica.ui.screens.PersonalInformationScreen.LocalInformationScreen
import com.proyecto.ReUbica.ui.screens.RegisterLocalScreen3
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.CreateProductoViewModel
import com.proyecto.ReUbica.utils.CreateProductoViewModelFactory

data class navItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun CustomScaffold(rootNavController: NavHostController){

    val context = LocalContext.current
    val userSessionManager = UserSessionManager(context.applicationContext)
    val registroComercioViewModel: RegistroComercioViewModel = viewModel()
    val createProductoViewModel: CreateProductoViewModel = viewModel(
        factory = CreateProductoViewModelFactory(userSessionManager)
    )

    val navController = rememberNavController()
    var title by rememberSaveable { mutableStateOf("Home") }
    var selectedItem by rememberSaveable { mutableStateOf("nowplaying") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val favoritosViewModel: FavoritosViewModel = viewModel()

    val showBars = currentRoute != LoadingScreenNavigation::class.qualifiedName

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
            if (showBars && currentRoute != ProfileScreenNavigation::class.qualifiedName &&
                currentRoute !=  PersonalDataNavigation::class.qualifiedName &&
                currentRoute != LocalInformationScreenNavigation::class.qualifiedName &&
                currentRoute != CartaProductosScreenNavigation::class.qualifiedName &&
                !currentRoute.orEmpty().startsWith("chat_comercio")
                ){
                TopBar(navController)
            }
        },
            bottomBar = {
                if (showBars){
                    BottomBar(
                        navItems = navItems,
                        selectedItem = selectedItem,
                        onItemSelected = { onItemSelected(it) }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column {
            NavHost(
                navController = navController,
                startDestination = HomeScreenNavigation,
                Modifier.padding(innerPadding)
            ) {

                composable<HomeScreenNavigation> {
                    HomeScreen(navController = navController)
                }

                composable<FavoritesScreenNavigation> {
                    FavoriteScreen(
                        navController = navController,
                        favoritosViewModel = favoritosViewModel
                    )
                }


                composable<SearchScreenNavigation>{
                    SearchScreen(navController = navController)
                }

                composable<ProfileScreenNavigation>{
                    ProfileScreen(
                        navController = navController,
                        rootNavController = rootNavController
                    )
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
                    RegisterLocalScreen1(navController, registroComercioViewModel, createProductoViewModel)
                }

                composable<RegisterLocalScreen2Navigation> {
                    RegisterLocalScreen2(navController, registroComercioViewModel, createProductoViewModel)
                }

                composable(
                    route = "${RegisterLocalScreen3Navigation.route}?${RegisterLocalScreen3Navigation.argIsAddingMore}={${RegisterLocalScreen3Navigation.argIsAddingMore}}",
                    arguments = listOf(
                        navArgument(RegisterLocalScreen3Navigation.argIsAddingMore) {
                            type = NavType.BoolType
                            defaultValue = false
                        }
                    )
                ) { backStackEntry ->
                    val isAddingMore = backStackEntry.arguments?.getBoolean(RegisterLocalScreen3Navigation.argIsAddingMore) == true
                    RegisterLocalScreen3(
                        navController = navController,
                        registroComercioViewModel = registroComercioViewModel,
                        createProductoViewModel = createProductoViewModel,
                        isAddingMoreProducts = isAddingMore
                    )
                }

                composable(LoadingScreenNavigation::class.qualifiedName ?: "") {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF49724C))
                    }
                }

                composable<LocalInformationScreenNavigation> {
                    LocalInformationScreen(navController)
                }

                composable<ComercioNavigation> { backStackEntry ->
                    val navArgs = backStackEntry.toRoute<ComercioNavigation>()

                    ComercioScreen(
                        navController = navController,
                        navArgs = navArgs.toEmprendimientoModel()
                    )
                }

                composable<CartaProductosScreenNavigation>{
                     CartaProductosScreen(navController)
                }

                composable(
                    route = "product_detail/{productId}?token={token}&emprendimientoID={emprendimientoID}",
                    arguments = listOf(
                        navArgument("productId") { type = NavType.StringType },
                        navArgument("token") { type = NavType.StringType },
                        navArgument("emprendimientoID") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    val token = backStackEntry.arguments?.getString("token") ?: ""
                    val emprendimientoID = backStackEntry.arguments?.getString("emprendimientoID") ?: ""

                    ProductDetailScreen(
                        productId = productId,
                        token = token,
                        emprendimientoID = emprendimientoID,
                        navController = navController
                    )
                }

            }
            Spacer(modifier = Modifier.padding(innerPadding))
        }
    }
}

