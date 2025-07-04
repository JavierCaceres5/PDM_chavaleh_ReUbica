package com.proyecto.ReUbica.ui.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation

@Composable
fun SeccionRestaurantes(
    titulo: String,
    emprendimientos: List<EmprendimientoModel>,
    favoritos: List<String>,
    onToggleFavorito: (String) -> Unit,
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            titulo,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(emprendimientos) { emprendimiento ->
                val id = emprendimiento.id?.toString() ?: return@items
                val logoUrl = if (isValidUrl(emprendimiento.logo)) emprendimiento.logo else null

                RestaurantCard(
                    nombre = emprendimiento.nombre ?: "Sin nombre",
                    departamento = emprendimiento.direccion ?: "Sin dirección",
                    categoria = emprendimiento.categoriasPrincipales?.firstOrNull() ?: "Sin categoría",
                    imagenRes = logoUrl,
                    isFavorito = favoritos.contains(id),
                    onFavoritoClick = { onToggleFavorito(id) },
                    onVerTiendaClick = {
                        val gson = Gson()
                        val redesJsonString = emprendimiento.redes_sociales?.let { redes ->
                            gson.toJson(
                                mapOf(
                                    "Instagram" to (redes.Instagram ?: ""),
                                    "Facebook" to (redes.Facebook ?: ""),
                                    "TikTok" to (redes.TikTok ?: ""),
                                    "Twitter" to (redes.Twitter ?: "")
                                ).filterValues { it.isNotBlank() }
                            )
                        } ?: ""

                        navController.navigate(
                            ComercioNavigation(
                                id = id,
                                nombre = emprendimiento.nombre ?: "Sin nombre",
                                descripcion = emprendimiento.descripcion ?: "Sin descripción",
                                categoriasPrincipales = emprendimiento.categoriasPrincipales ?: emptyList(),
                                direccion = emprendimiento.direccion ?: "Sin dirección",
                                latitud = emprendimiento.latitud ?: 0.0,
                                longitud = emprendimiento.longitud ?: 0.0,
                                categoriasSecundarias = emprendimiento.categoriasSecundarias ?: emptyList(),
                                logo = emprendimiento.logo ?: "",
                                emprendimientoPhone = emprendimiento.emprendimientoPhone ?: "",
                                redessociales = redesJsonString,
                                userID = emprendimiento.userID.toString(),
                                createdat = emprendimiento.created_at.toString(),
                                updatedat = emprendimiento.updated_at.toString()
                            )
                        )
                    }
                )
            }
        }
    }
}

fun isValidUrl(url: String?): Boolean =
    url != null && (url.startsWith("http://") || url.startsWith("https://"))
