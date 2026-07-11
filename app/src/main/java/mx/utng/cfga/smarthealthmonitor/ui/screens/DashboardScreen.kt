package mx.utng.cfga.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.cfga.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.cfga.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.cfga.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.cfga.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.cfga.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(title: String) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        actions = {
            // CastButton: AndroidView que envuelve MediaRouteButton
            AndroidView(
                factory = { context ->
                    MediaRouteButton(context).apply {
                        CastButtonFactory.setUpMediaRouteButton(context, this)
                    }
                },
                modifier = Modifier.size(48.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit,
    onAlertClick: () -> Unit, // Se mantiene por compatibilidad con la firma, pero el flujo principal usa el diálogo local
    viewModel: DashboardViewModel = viewModel()
) {
    // Escuchamos el estado del StateFlow en tiempo real usando el ViewModel
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()

    // ── ESTADO DEL DIÁLOGO Y SNACKBAR (EJERCICIO 02 + RETOS) ───────────────────
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ── DIÁLOGO CONDICIONAL CON RETO DE NOTA OPCIONAL ─────────────────────────
    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = { nota ->
                mostrarAlerta = false
                scope.launch {
                    // Mensaje dinámico dependiendo de si el usuario escribió una nota o no
                    val mensaje = if (nota.isBlank()) {
                        "✅ Alerta Enviada a tus contactos de emergencia"
                    } else {
                        "✅ Alerta Enviada: \"$nota\""
                    }

                    // Lanzamos el Snackbar con la opción de Deshacer (Reto Adicional)
                    val resultado = snackbarHost.showSnackbar(
                        message = mensaje,
                        actionLabel = "Deshacer",
                        duration = SnackbarDuration.Long
                    )

                    when (resultado) {
                        SnackbarResult.ActionPerformed -> {
                            // Si presionó el botón de acción "Deshacer"
                            snackbarHost.showSnackbar(
                                message = "❌ Alerta cancelada correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }
                        SnackbarResult.Dismissed -> {
                            // Desapareció de forma automática tras el timeout estándar
                        }
                    }
                }
            }
        )
    }

    SmartHealthMonitorTheme {
        Scaffold(
            // ── CONTROL DEL SNACKBAR ASIGNADO AL SCAFFOLD ──────────────────────────
            snackbarHost = { SnackbarHost(hostState = snackbarHost) },
            topBar = {
                DashboardTopBar(title = "SmartHealth")
            },
            // ── BOTÓN FLOTANTE ROJO DE EMERGENCIA COMPATIBLE CON WCAG (48dp) ───────
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarAlerta = true }, // Levanta el AlertDialog localmente
                    containerColor = MaterialTheme.colorScheme.error, // Usamos color de paleta pura MD3
                    modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint = MaterialTheme.colorScheme.onError
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
                        Text(
                            text = "Historial Reciente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(
                            onClick = onHistorialClick,
                            modifier = Modifier.defaultMinSize(minHeight = 48.dp) // WCAG AA 48dp táctil
                        ) {
                            Text(text = "Ver todo")
                        }
                    }
                }

                // Listado de historial dinámico de Room
                items(historial) { lectura ->
                    FilaHistorial(lectura = lectura)
                }

                // ============================================================
                // EJERCICIO 03: SIMULADOR DE DATOS DE WEARABLE (DEBUG)
                // ============================================================
                item {
                    OutlinedButton(
                        onClick = {
                            val fcSimulado = (60..110).random()
                            val pasosSimulados = (3000..9000).random()

                            scope.launch {
                                SmartHealthRepository.actualizarFC(fcSimulado)
                            }
                            SmartHealthRepository.actualizarPasos(pasosSimulados)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .defaultMinSize(minHeight = 48.dp) // WCAG AA 48dp táctil
                    ) {
                        Text("Simular dato del wearable (DEBUG)")
                    }
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