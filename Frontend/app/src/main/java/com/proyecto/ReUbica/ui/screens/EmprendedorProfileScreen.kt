package com.proyecto.ReUbica.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.ui.navigations.LegalInformationNavigation
import com.proyecto.ReUbica.ui.navigations.LoadingScreenNavigation
import com.proyecto.ReUbica.ui.navigations.NotificationsNavigation
import com.proyecto.ReUbica.ui.navigations.PersonalDataNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegistroNavigation
import com.proyecto.ReUbica.ui.navigations.WelcomeScreenNavigation
import com.proyecto.ReUbica.ui.screens.ProfileScreen.ProfileScreenViewModel

@Composable
fun EmprendedorProfileScreen(navController: NavHostController, rootNavController: NavHostController){


    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }
    val profileViewModel: ProfileScreenViewModel = viewModel()

    val scrollState = rememberScrollState()
    val showConfirmLogOut = remember { mutableStateOf(false) }
    val showSuccessLogOut = remember { mutableStateOf(false) }
    val showConfirmDelete = remember { mutableStateOf(false) }

    val loading by profileViewModel.loading.collectAsState()
    var navigatedToLoading by remember { mutableStateOf(false) }

    LaunchedEffect(loading) {
        if (loading && !navigatedToLoading) {
            navController.navigate(LoadingScreenNavigation::class.qualifiedName ?: "") {
                popUpTo(ProfileScreenNavigation::class.qualifiedName ?: "") { inclusive = true }
            }
            navigatedToLoading = true
        } else if (!loading && navigatedToLoading) {
            navController.navigate(ProfileScreenNavigation::class.qualifiedName ?: "") {
                popUpTo(LoadingScreenNavigation::class.qualifiedName ?: "") { inclusive = true }
            }
            navigatedToLoading = false
        }
    }

    if (loading) return

    Column (modifier = Modifier.verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ){
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

            Spacer(modifier = Modifier.height(40.dp))

            Column (
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
                ListItemRow("Cerrar sesi贸n",  onClick = { showConfirmLogOut.value = true },  icon = Icons.Filled.ArrowOutward)
                ListItemRow("Eliminar cuenta",  onClick = { showConfirmDelete.value = true },  icon = Icons.Filled.ArrowOutward)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mi negocio",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )

                ListItemRow("Informaci贸n de local", onClick = { }, icon = Icons.Filled.ArrowOutward)
                ListItemRow("Carta de productos",  onClick = { },  icon = Icons.Filled.ArrowOutward)
                ListItemRow("Eliminar negocio",  onClick = { },  icon = Icons.Filled.ArrowOutward)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Actividad",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )

                ListItemRow("Notificaciones", onClick = { navController.navigate(NotificationsNavigation)}, icon = Icons.Filled.ArrowOutward)

                Text(
                    text = "Configuraci贸n",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )

                ListItemRow("Informaci贸n legal", onClick = { navController.navigate(LegalInformationNavigation)}, icon = Icons.Filled.ArrowOutward)
            }
        }
    }

    if (showConfirmDelete.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showConfirmDelete.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDelete.value = false
                        profileViewModel.deleteAccount {
                            rootNavController.navigate(RegistroNavigation) {
                                popUpTo(0) { inclusive = true }
                            }
                        }                    },
                    modifier = Modifier.width(130.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E210B), contentColor = Color.White)
                ) {
                    Text("Eliminar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showConfirmDelete.value = false
                    },
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
                        "Est谩s a punto de eliminar una cuenta de estado activo. Todos tus datos guardados ser谩n eliminados permanentemente. Esta acci贸n no se puede deshacer.",
                        textAlign = TextAlign.Center, color = Color.Black
                    )
                }
            }
        )
    }
}

