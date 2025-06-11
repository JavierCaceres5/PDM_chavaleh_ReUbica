package com.proyecto.ReUbica.product

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductManager {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://ahieefmrpftunlcbrphg.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFoaWVlZm1ycGZ0dW5sY2JycGhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDkwMDU0NzEsImV4cCI6MjA2NDU4MTQ3MX0.l_GTQP75viLCM7BV0ZVQVyfVZqqUOTDCTgWJZSd28sg"
    ) {
        install(io.github.jan.supabase.postgrest.Postgrest)
    }

    fun getAllProducts(): Flow<ProductResponse> = flow {
        try {
            val result = supabase.from("Productos").select().decodeList<Product>()
            emit(ProductResponse.Success(result))
        } catch (e: Exception) {
            emit(ProductResponse.Error(e.localizedMessage))
        }
    }

    fun addProduct(product: Product): Flow<ProductResponse> = flow {
        try {
            supabase.from("Productos").insert(product)
            emit(ProductResponse.Success(null))
        } catch (e: Exception) {
            emit(ProductResponse.Error(e.localizedMessage))
        }
    }

    fun deleteProduct(productId: Int): Flow<ProductResponse> = flow {
        try {
            supabase.from("Productos").delete {
                filter {
                    eq("id", productId)
                }
            }
            emit(ProductResponse.Success(null))
        } catch (e: Exception) {
            emit(ProductResponse.Error(e.localizedMessage))
        }
    }

    fun updateProduct(product: Product): Flow<ProductResponse> = flow {
        try {
            val updateDTO = ProductUpdateDTO(
                name = product.name,
                description = product.description,
                price = product.price,
                image_url = product.image_url
            )

            supabase.from("Productos").update(updateDTO) {
                filter {
                    eq("id", product.id)
                }
            }

            emit(ProductResponse.Success(null))
        } catch (e: Exception) {
            emit(ProductResponse.Error(e.localizedMessage))
        }
    }
}
