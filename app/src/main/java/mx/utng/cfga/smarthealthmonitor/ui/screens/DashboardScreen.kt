// ui/screens/DashboardScreen.kt
package mx.utng.cfga.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // <-- NUEVO IMPORT
import androidx.compose.runtime.getValue         // <-- NUEVO IMPORT
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // <-- NUEVO IMPORT
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository // <-- NUEVO IMPORT PARA EL SIMULADOR
import mx.utng.cfga.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.cfga.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.cfga.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.cfga.smarthealthmonitor.ui.viewmodel.DashboardViewModel // <-- NUEVO IMPORT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit,
    onAlertClick: () -> Unit,
    viewModel: DashboardViewModel = viewModel() // <-- CAMBIO AQUÍ: Inyección automática de la Sesión 6
) {
    // Escuchamos el estado del StateFlow en tiempo real usando el ViewModel
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "SmartHealth", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAlertClick,
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Enviar alerta de emergencia",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tarjeta Frecuencia Cardíaca
            item {
                TarjetaDato(
                    valor = "$fc",
                    unidad = "bpm",
                    label = "Frecuencia cardíaca",
                    colorValor = if (fc > 90) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
            // Tarjeta Pasos
            item {
                TarjetaDato(
                    valor = "%,d".format(pasos),
                    unidad = "pasos",
                    label = "Pasos del día",
                    colorValor = MaterialTheme.colorScheme.primary
                )
            }
            // Encabezado Historial
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Historial Reciente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onHistorialClick) {
                        Text(text = "Ver todo")
                    }
                }
            }
            // Listado de historial dinámico
            items(historial) { lectura ->
                FilaHistorial(lectura = lectura)
            }

            // ============================================================
            // EJERCICIO 03: SIMULADOR DE DATOS DE WEARABLE (DEBUG)
            // ============================================================
            item {
                OutlinedButton(
                    onClick = {
                        // Generamos números aleatorios para probar la reactividad
                        val fcSimulado = (60..110).random()
                        val pasosSimulados = (3000..9000).random()

                        // Notificamos directamente al repositorio centralizado
                        scope.launch {
                            SmartHealthRepository.actualizarFC(fcSimulado)
                        }
                        SmartHealthRepository.actualizarPasos(pasosSimulados)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Simular dato del wearable (DEBUG)")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen(onHistorialClick = {}, onAlertClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardScreenDarkPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen(onHistorialClick = {}, onAlertClick = {})
    }
}