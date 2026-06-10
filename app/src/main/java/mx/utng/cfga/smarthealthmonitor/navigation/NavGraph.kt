package mx.utng.cfga.smarthealthmonitor.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.utng.cfga.smarthealthmonitor.LoginScreen
import mx.utng.cfga.smarthealthmonitor.ui.screens.DashboardScreen
import mx.utng.cfga.smarthealthmonitor.ui.screens.HistorialScreen // 👇 IMPORTANTE: Asegúrate de que esta ruta coincida con tu paquete
import mx.utng.cfga.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun SmartHealthNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onHistorialClick = {
                    navController.navigate(Screen.Historial.route)
                },
                onAlertClick = {
                    navController.navigate(Screen.Alerta.route)
                }
            )
        }

        // 👇 CORREGIDO: Ahora conecta directamente con la interfaz reactiva de Room
        composable(Screen.Historial.route) {
            HistorialScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Se mantiene en construcción ya que la práctica solo pide Room para el Historial
        composable(Screen.Alerta.route) {
            PantallaEnConstruccion(
                titulo = "Enviar alerta",
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    onBack: () -> Unit
) {
    SmartHealthMonitorTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(titulo)
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Próximamente: $titulo",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}