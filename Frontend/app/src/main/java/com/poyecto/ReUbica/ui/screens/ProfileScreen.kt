package com.poyecto.ReUbica.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.poyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.LegalInformationNavigation
import com.proyecto.ReUbica.ui.navigations.NotificationsNavigation
import com.proyecto.ReUbica.ui.navigations.PersonalDataNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalNavigation

@Composable
fun ProfileScreen(navController: NavHostController) {

    val showConfirmLogOut = remember { mutableStateOf(false) }
    val showSuccessLogOut = remember { mutableStateOf(false) }
    val showConfirmDelete = remember { mutableStateOf(false) }
    val showSuccessDelete = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 30.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .padding(bottom = 25.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Perfil",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )

            ListItemRow("Datos personales", onClick = { navController.navigate(PersonalDataNavigation) }, icon = Icons.Filled.ArrowOutward)
            ListItemRow("Cerrar sesión",  onClick = { showConfirmLogOut.value = true },  icon = Icons.Filled.ArrowOutward)
            ListItemRow("Eliminar cuenta",  onClick = { showConfirmDelete.value = true },  icon = Icons.Filled.ArrowOutward)

            Text(
                text = "Actividad",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )

            ListItemRow("Registrar negocio",  onClick = { navController.navigate(RegisterLocalNavigation) },  icon = Icons.Filled.ArrowOutward)
            ListItemRow("Notificaciones",  onClick = { navController.navigate(NotificationsNavigation) },  icon = Icons.Filled.ArrowOutward)

            Text(
                text = "Configuración",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A3C1D),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 15.dp)
            )

            ListItemRow("Información legal",  onClick = { navController.navigate(LegalInformationNavigation) },  icon = Icons.Filled.ArrowOutward)
        }
    }

    if (showConfirmLogOut.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showConfirmLogOut.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmLogOut.value = false
                        showSuccessLogOut.value = true
                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E210B), contentColor = Color.White)
                ) {
                    Text("Cerrar sesión", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmLogOut.value = false },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            },
            title = { Text("¿Estás seguro que deseas cerrar sesión?", fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center, color = Color.Black) },
            text = { Text("Tendrás que volver a iniciar sesión para realizar cualquier actividad.", textAlign = TextAlign.Center, color = Color.Black) }
        )
    }

    if (showSuccessLogOut.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showSuccessLogOut.value = false },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { showSuccessLogOut.value = false },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                    ) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("¡Todo listo!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Se ha cerrado sesión con éxito.", textAlign = TextAlign.Center, color = Color.Black)
                }
            }
        )
    }

    if (showConfirmDelete.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showConfirmDelete.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDelete.value = false
                        showSuccessDelete.value = true
                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E210B), contentColor = Color.White)
                ) {
                    Text("Eliminar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDelete.value = false },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Eliminar cuenta", fontWeight = FontWeight.Bold, fontSize = 24.sp, textAlign = TextAlign.Center, color = Color.Black)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "ADVERTENCIA",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF8E210B),
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Estás a punto de eliminar una cuenta de estado activo. Todos tus datos guardados serán eliminados permanentemente. Esta acción no se puede deshacer.",
                        textAlign = TextAlign.Center, color = Color.Black
                    )
                }
            }
        )
    }

    if (showSuccessDelete.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showSuccessDelete.value = false },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { showSuccessDelete.value = false },
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                    ) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Tu cuenta ha sido eliminada con éxito.",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            },
        )
    }
}
