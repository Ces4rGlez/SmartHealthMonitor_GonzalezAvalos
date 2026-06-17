package mx.utng.cfga.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.*
import kotlinx.coroutines.launch
import mx.utng.cfga.smarthealthmonitor.wear.presentation.components.WearFCCard

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams

@Composable
fun WearDashboardScreen(
    viewModel: WearDashboardViewModel,
    onAlertClick: () -> Unit = {},
    onHistorialClick: () -> Unit = {} // 👈 AGREGADO: Callback para la navegación del Ejercicio 01
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val listState = rememberScalingLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataSender = remember { WearDataSender(context) }

    var sliderValue by remember { mutableFloatStateOf(fc.toFloat()) }

    ScreenScaffold(
        scrollState = listState,
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 28.dp, bottom = 45.dp, start = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            autoCentering = AutoCenteringParams(itemIndex = 1)
        ) {
            // Título de sección minimalista
            item {
                Text(
                    text = "VITAL SIGNS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Tarjeta de Frecuencia Cardíaca (Diseño enfocado)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WearFCCard(fc = fc, modifier = Modifier.fillMaxWidth())
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Simulador con diseño de "Herramienta de Diagnóstico"
            item {
                Card(
                    onClick = {},
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Simular",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "${sliderValue.toInt()} BPM",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Slider(
                            value = sliderValue,
                            onValueChange = {
                                sliderValue = it
                                val bpm = it.toInt()
                                scope.launch {
                                    SmartHealthRepository.actualizarFC(bpm)
                                    dataSender.enviarFC(bpm)
                                }
                            },
                            valueRange = 40f..200f,
                            steps = 160,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Sección de Actividad - Estilo Moderno
            item {
                TitleCard(
                    onClick = { },
                    title = {
                        Text(
                            "🏃 ACTIVIDAD",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (pasos == 0) "0 pasos" else "$pasos pasos",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── NUEVO: Botón de Historial con Material 3 ───────────────────
            item {
                Card(
                    onClick = onHistorialClick,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("📋", fontSize = 16.sp)
                        Text(
                            text = "Historial Clínico",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Botón SOS de Emergencia - Look "Actionable"
            item {
                Button(
                    onClick = onAlertClick,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🚨", fontSize = 18.sp)
                        Text(
                            text = "SOS EMERGENCY",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}