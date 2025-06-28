package com.proyecto.ReUbica.ui.screens.SearchScreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.ui.screens.EmprendimientosBuscar.SearchScreenViewModel
import com.proyecto.ReUbica.ui.screens.FavoriteScreen.FavoritosViewModel
import com.proyecto.ReUbica.ui.screens.HomeScreen.SeccionRestaurantes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    favoritosViewModel: FavoritosViewModel = viewModel(),
    categoriaPreseleccionada: String? = null
) {
    val viewModel: SearchScreenViewModel = viewModel()
    val resultadosApi by viewModel.resultadosByNombre.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }

    LaunchedEffect(Unit) {
        viewModel.setUserSessionManager(userSessionManager)
    }

    var searchQuery by remember { mutableStateOf(categoriaPreseleccionada ?: "") }
    var searchHistory by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val destacadosMock = listOf(
        Triple("El Panalito", "Chinameca", "Alimentos"),
        Triple("Solemare", "Santa Ana", "Artesanías"),
        Triple("Pizza Ranch", "San Miguel", "Comida"),
        Triple("Arte Maya", "Ahuachapán", "Artesanías")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //  Buscador
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF7F8EF))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        debounceJob?.cancel()
                        debounceJob = coroutineScope.launch {
                            delay(2000)
                            if (it.isNotBlank() && it !in searchHistory) {
                                searchHistory = listOf(it) + searchHistory.take(4)
                            }
                            if (it.isNotBlank()) {
                                viewModel.searchEmprendimientoByNombre(it)
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier.weight(1f)
                )
                if (searchQuery.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { searchQuery = "" },
                        tint = Color.Black
                    )
                }
            }
        }

        //  Historial
        if (searchHistory.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                searchHistory.forEach { query ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = query,
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp
                        )
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.clickable {
                                searchHistory = searchHistory - query
                            },
                            tint = Color.Gray
                        )
                    }
                }
            }
        }


        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }

            !error.isNullOrEmpty() -> {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            resultadosApi.isNotEmpty() -> {
                val resultados = resultadosApi.map {
                    Triple(
                        it.nombre ?: "Sin nombre",
                        it.direccion ?: "Sin dirección",
                        it.categoriasPrincipales.firstOrNull() ?: "Sin categoría"
                    )
                }

                SeccionRestaurantes(
                    titulo = "Resultados de búsqueda",
                    destacados = resultados,
                    favoritosViewModel = favoritosViewModel,
                    navController = navController
                )
            }

            searchQuery.isNotBlank() && resultadosApi.isEmpty() -> {
                Text(
                    text = "No se encontraron resultados para \"$searchQuery\".",
                    modifier = Modifier.padding(16.dp)
                )
            }


            searchQuery.isBlank() -> {
                SeccionRestaurantes(
                    titulo = "Pueden interesarte",
                    destacados = destacadosMock,
                    favoritosViewModel = favoritosViewModel,
                    navController = navController
                )

                SeccionRestaurantes(
                    titulo = "Nuevos emprendedores",
                    destacados = destacadosMock,
                    favoritosViewModel = favoritosViewModel,
                    navController = navController
                )
            }
        }
    }
}

// Conservamos esta clase porque se usa en HomeScreen en lo que hacemos esa conexión al backend es de comercios destacadso -Carlos
data class CategoriaItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)
