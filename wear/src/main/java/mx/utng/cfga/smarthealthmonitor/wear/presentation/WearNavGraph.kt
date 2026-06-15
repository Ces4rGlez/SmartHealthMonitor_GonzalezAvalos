package mx.utng.cfga.smarthealthmonitor.wear.presentation

import androidx.compose.runtime.*
import androidx.wear.compose.material3.*

enum class WearScreen {
    DASHBOARD, ALERTA
}

@Composable
fun SmartHealthWearNavGraph(viewModel: WearDashboardViewModel) {
    var currentScreen by remember { mutableStateOf(WearScreen.DASHBOARD) }

    AppScaffold {
        when (currentScreen) {
            WearScreen.DASHBOARD -> {
                WearDashboardScreen(
                    viewModel = viewModel,
                    onAlertClick = { currentScreen = WearScreen.ALERTA }
                )
            }
            WearScreen.ALERTA -> {
                val fc by viewModel.fc.collectAsState()
                WearAlertaScreen(
                    fc = fc,
                    onConfirmar = { currentScreen = WearScreen.DASHBOARD },
                    onCancelar = { currentScreen = WearScreen.DASHBOARD }
                )
            }
        }
    }
}
