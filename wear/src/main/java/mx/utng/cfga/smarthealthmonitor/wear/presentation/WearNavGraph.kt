package mx.utng.cfga.smarthealthmonitor.wear.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

// 👇 CORREGIDO: Estructura de constantes centralizada para las rutas de navegación
object WearScreens {
    const val DASHBOARD = "wear_dashboard"
    const val ALERTA    = "wear_alerta"
    const val HISTORIAL = "wear_historial"
}

@Composable
fun SmartHealthWearNavGraph() {
    // 👇 CORREGIDO: Controlador nativo que habilita el gesto de deslizar a la derecha para regresar
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController    = navController,
        startDestination = WearScreens.DASHBOARD
    ) {
        // Destino 1: Dashboard Principal del Reloj
        composable(WearScreens.DASHBOARD) {
            WearDashboardScreen(
                onAlertClick = {
                    navController.navigate(WearScreens.ALERTA)
                },
                onHistorialClick = { // Vinculación con el nuevo chip del historial de la S10
                    navController.navigate(WearScreens.HISTORIAL)
                }
            )
        }

        // Destino 2: Pantalla de Confirmación de Alerta Crítica
        composable(WearScreens.ALERTA) {
            val vm: WearDashboardViewModel = viewModel()
            val fc by vm.fc.collectAsState()

            WearAlertaScreen(
                fc          = fc,
                onConfirmar = { navController.popBackStack() },
                onCancelar  = { navController.popBackStack() }
            )
        }

        // 👇 NUEVO DESTINO SESIÓN 10: Pantalla de Historial con Rotary Input
        composable(WearScreens.HISTORIAL) {
            WearHistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}