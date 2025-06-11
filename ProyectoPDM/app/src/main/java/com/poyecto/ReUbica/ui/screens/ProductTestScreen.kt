package com.proyecto.ReUbica.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.proyecto.ReUbica.product.Product
import com.proyecto.ReUbica.ui.viewmodel.ProductViewModel

@Composable
fun ProductTestScreen(viewModel: ProductViewModel = viewModel()) {
    val products by viewModel.products.collectAsState()
    val error by viewModel.error.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var editingProductId by remember { mutableStateOf<Int?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUrl = uri?.toString() ?: ""
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Nombre del producto", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.DarkGray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Descripción del producto", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.DarkGray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Precio", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("$0.00") },
            textStyle = LocalTextStyle.current.copy(color = Color.DarkGray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Adjuntar imagen", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { imagePicker.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        if (imageUrl.isNotBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Vista previa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            if (editingProductId == null) {
                Button(onClick = {
                    val parsedPrice = price.toDoubleOrNull() ?: 0.0
                    val newProduct = Product(
                        name = name,
                        description = description,
                        price = parsedPrice,
                        image_url = imageUrl
                    )
                    viewModel.addProduct(newProduct)
                    name = ""
                    description = ""
                    price = ""
                    imageUrl = ""
                }) {
                    Text("Agregar")
                }
            } else {
                Button(onClick = {
                    val parsedPrice = price.toDoubleOrNull() ?: 0.0
                    val updatedProduct = Product(
                        id = editingProductId!!,
                        name = name,
                        description = description,
                        price = parsedPrice,
                        image_url = imageUrl
                    )
                    viewModel.updateProduct(updatedProduct)
                    editingProductId = null
                    name = ""
                    description = ""
                    price = ""
                    imageUrl = ""
                }) {
                    Text("Guardar cambios")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Nombre: ${product.name}", style = MaterialTheme.typography.bodyLarge)
                        Text("Descripción: ${product.description}", style = MaterialTheme.typography.bodyMedium)
                        Text("Precio: $${product.price}", style = MaterialTheme.typography.bodyMedium)

                        if (product.image_url.isNotBlank()) {
                            AsyncImage(
                                model = product.image_url,
                                contentDescription = "Imagen del producto",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Button(onClick = {
                                editingProductId = product.id
                                name = product.name
                                description = product.description
                                price = product.price.toString()
                                imageUrl = product.image_url
                            }) {
                                Text("Editar")
                            }
                            Button(
                                onClick = { viewModel.deleteProduct(product.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        error?.let {
            Text("Error: $it", color = Color.Red)
        }
    }
}
