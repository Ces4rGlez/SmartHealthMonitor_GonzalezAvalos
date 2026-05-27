package mx.utng.cfga.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC
import mx.utng.cfga.smarthealthmonitor.data.models.MockData
import mx.utng.cfga.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.cfga.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.cfga.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit,
    onAlertClick: () -> Unit,
    fc: Int = MockData.fcActual,
    pasos: Int = MockData.pasosActual,
    historial: List<LecturaFC> = MockData.historialFC
) {
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