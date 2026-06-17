package mx.utng.cfga.smarthealthmonitor.wear.presentation

import androidx.compose.runtime.*
import androidx.wear.compose.material3.*

enum class WearScreens {
    DASHBOARD, ALERTA, HISTORIAL
}

@Composable
fun SmartHealthWearNavGraph(viewModel: WearDashboardViewModel) {
    var currentScreen by remember { mutableStateOf(WearScreens.DASHBOARD) }

    // Implementación manual de navegación para evitar dependencias faltantes
    AppScaffold {
        when (currentScreen) {
            WearScreens.DASHBOARD -> {
                WearDashboardScreen(
                    viewModel = viewModel,
                    onAlertClick = { currentScreen = WearScreens.ALERTA },
                    onHistorialClick = { currentScreen = WearScreens.HISTORIAL }
                )
            }
            WearScreens.ALERTA -> {
                val fc by viewModel.fc.collectAsState()
                WearAlertaScreen(
                    fc = fc,
                    onConfirmar = { currentScreen = WearScreens.DASHBOARD },
                    onCancelar = { currentScreen = WearScreens.DASHBOARD }
                )
            }
            WearScreens.HISTORIAL -> {
                WearHistorialScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = WearScreens.DASHBOARD }
                )
            }
        }
    }
}
