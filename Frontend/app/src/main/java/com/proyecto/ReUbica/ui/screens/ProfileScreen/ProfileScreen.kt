package com.proyecto.ReUbica.ui.screens.ProfileScreen

import android.app.Application
import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.ReUbica.ui.Components.ListItemRow
import com.proyecto.ReUbica.R
import com.proyecto.ReUbica.ui.navigations.LegalInformationNavigation
import com.proyecto.ReUbica.ui.navigations.NotificationsNavigation
import com.proyecto.ReUbica.ui.navigations.PersonalDataNavigation
import com.proyecto.ReUbica.ui.navigations.RegisterLocalNavigation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.proyecto.ReUbica.data.local.UserSessionManager
import com.proyecto.ReUbica.ui.navigations.CartaProductosScreenNavigation
import com.proyecto.ReUbica.ui.navigations.HomeScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LoadingScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LocalInformationScreenNavigation
import com.proyecto.ReUbica.ui.navigations.LoginScreenNavigation
import com.proyecto.ReUbica.ui.navigations.ProfileScreenNavigation
import com.proyecto.ReUbica.ui.navigations.RegistroNavigation
import com.proyecto.ReUbica.ui.screens.LoginScreen.LoginScreenViewModel
import com.proyecto.ReUbica.ui.screens.RegistroComercioScreens.RegistroComercioViewModel
import com.proyecto.ReUbica.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController, rootNavController: NavHostController
)
{


    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }

    val profileViewModel: ProfileScreenViewModel = viewModel()

    val showConfirmLogOut = remember { mutableStateOf(false) }
    val showSuccessLogOut = remember { mutableStateOf(false) }
    val showConfirmDeleteAccount = remember { mutableStateOf(false) }
    val showConfirmDeleteEmprendimiento = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val loading by profileViewModel.loading.collectAsState()
    val showBlockedDeleteAccountDialog = remember { mutableStateOf(false) }

    val user by profileViewModel.user.collectAsState()
    val rol = user?.user_role

    val negocioEliminado by profileViewModel.negocioEliminado.collectAsState()

    LaunchedEffect(negocioEliminado) {
        if (negocioEliminado) {
            profileViewModel.resetNegocioEliminado()
        }
    }

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

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF49724C))
            }
        } else {
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

                ListItemRow(
                    "Datos personales",
                    onClick = { navController.navigate(PersonalDataNavigation) },
                    icon = Icons.Filled.ArrowOutward
                )
                ListItemRow(
                    "Cerrar sesión",
                    onClick = { showConfirmLogOut.value = true },
                    icon = Icons.Filled.ArrowOutward
                )
                ListItemRow(
                    "Eliminar cuenta",
                    onClick = {
                        if (rol == "emprendedor") {
                            showBlockedDeleteAccountDialog.value = true
                        } else {
                            showConfirmDeleteAccount.value = true
                        }
                    },
                    icon = Icons.Filled.ArrowOutward
                )

                if (rol == "cliente") {

                    Text(
                        text = "Actividad",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5A3C1D),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                    )

                    ListItemRow(
                        text = "Registrar negocio",
                        onClick = { navController.navigate(RegisterLocalNavigation) },
                        icon = Icons.Filled.ArrowOutward
                    )
                } else if (rol == "emprendedor") {
                    Text(
                        text = "Mi negocio",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5A3C1D),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                    )
                    ListItemRow(
                        text = "Información de local",
                        onClick = { navController.navigate(LocalInformationScreenNavigation) },
                        icon = Icons.Filled.ArrowOutward
                    )
                    ListItemRow(
                        text = "Carta de productos",
                        onClick = {
                            navController.navigate(CartaProductosScreenNavigation)
                        },
                        icon = Icons.Filled.ArrowOutward
                    )
                    ListItemRow(
                        text = "Eliminar negocio",
                        onClick = {
                            showConfirmDeleteEmprendimiento.value = true
                        },
                        icon = Icons.Filled.ArrowOutward
                    )
                }

                Text(
                    text = "Configuración",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A3C1D),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )

                ListItemRow(
                    "Información legal",
                    onClick = { navController.navigate(LegalInformationNavigation) },
                    icon = Icons.Filled.ArrowOutward
                )
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
                            CoroutineScope(Dispatchers.IO).launch {
                                sessionManager.clearSession()
                                launch(Dispatchers.Main) {
                                    rootNavController.navigate(LoginScreenNavigation) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
                            showSuccessLogOut.value = true
                            Log.d("ProfileScreen", "User logged out successfully")
                        },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8E210B),
                            contentColor = Color.White
                        )
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                },
                title = {
                    Text(
                        "¿Estás seguro que deseas cerrar sesión?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                },
                text = {
                    Text(
                        "Tendrás que volver a iniciar sesión para realizar cualquier actividad.",
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            )
        }

        if (showConfirmDeleteAccount.value) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showConfirmDeleteAccount.value = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDeleteAccount.value = false
                            profileViewModel.deleteAccount {
                                rootNavController.navigate(RegistroNavigation) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8E210B),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Eliminar", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showConfirmDeleteAccount.value = false
                        },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Eliminar cuenta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
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

        if (showBlockedDeleteAccountDialog.value) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showBlockedDeleteAccountDialog.value = false },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) { Button(
                        onClick = { showBlockedDeleteAccountDialog.value = false },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8E210B),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Entendido", fontWeight = FontWeight.Bold)
                    }
                }
                                },
                title = {
                    Text(
                        "No puedes eliminar tu cuenta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                },
                text = {
                    Text(
                        "Para eliminar tu cuenta primero debes eliminar tu emprendimiento. Dirígete a la sección \"Eliminar negocio\" en tu perfil.",
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                },

            )
        }


        if (showConfirmDeleteEmprendimiento.value) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showConfirmDeleteEmprendimiento.value = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDeleteEmprendimiento.value = false
                            profileViewModel.deleteMiEmprendimiento()
                        },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8E210B),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Eliminar", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showConfirmDeleteEmprendimiento.value = false
                        },
                        modifier = Modifier.width(130.dp),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Eliminar emprendimiento",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
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
                            "Estás a punto de eliminar un emprendimiento de estado activo. Todos la información y datos almacenados serán eliminados permanentemente. Esta acción no se puede deshacer.",
                            textAlign = TextAlign.Center, color = Color.Black
                        )
                    }
                }
            )
        }
    }
}