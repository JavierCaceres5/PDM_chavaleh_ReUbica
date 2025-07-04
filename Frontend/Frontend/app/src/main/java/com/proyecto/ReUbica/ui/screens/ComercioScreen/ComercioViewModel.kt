package com.proyecto.ReUbica.ui.screens.ComercioScreen

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.proyecto.ReUbica.data.model.DummyProduct
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class BusinessDetailState(
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val locationName: String = "",
    val location: LatLng = LatLng(0.0, 0.0),
    val hours: String = "8:00 AM - 5:00 PM",
    val products: List<DummyProduct> = emptyList()
)

class ComercioViewModel : ViewModel() {

    private val _business = MutableStateFlow(BusinessDetailState())
    val business: StateFlow<BusinessDetailState> = _business

    fun setBusinessInfo(navArgs: ComercioNavigation) {
        _business.update {
            it.copy(
                name = navArgs.nombre,
                description = navArgs.descripcion,
                category = navArgs.categoria,
                locationName = navArgs.direccion,
                location = LatLng(navArgs.latitud, navArgs.longitud),
                products = listOf(
                    DummyProduct(
                        id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        name = "Café especial",
                        description = "Café de finca salvadoreña, 100% arábica.",
                        price = 1.75,
                        rating = 4.8f
                    ),
                    DummyProduct(
                        id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
                        name = "Empanada de frijol",
                        description = "Empanada dulce rellena de frijol con azúcar.",
                        price = 0.50,
                        rating = 4.5f
                    ),
                    DummyProduct(
                        id = UUID.fromString("00000000-0000-0000-0000-000000000003"),
                        name = "Quesadilla salvadoreña",
                        description = "Repostería típica hecha con queso y crema.",
                        price = 1.25,
                        rating = 4.7f
                    ),
                    DummyProduct(
                        id = UUID.fromString("00000000-0000-0000-0000-000000000004"),
                        name = "Tamales de pollo",
                        description = "Tamales de pollo con masa suave y especias locales.",
                        price = 0.90,
                        rating = 4.6f
                    ),
                    DummyProduct(
                        id = UUID.fromString("00000000-0000-0000-0000-000000000005"),
                        name = "Atol de elote",
                        description = "Bebida caliente de maíz dulce, tradicional.",
                        price = 0.75,
                        rating = 4.9f
                    )
                )
            )
        }
    }
}
