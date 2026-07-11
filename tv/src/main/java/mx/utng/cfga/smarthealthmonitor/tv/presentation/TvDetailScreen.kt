package mx.utng.cfga.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.*
import mx.utng.cfga.smarthealthmonitor.data.models.LecturaFC
import mx.utng.cfga.smarthealthmonitor.tv.TvViewModel
import mx.utng.cfga.smarthealthmonitor.tv.TvViewModelFactory // Asegura tener tu Factory si el ViewModel recibe contexto

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvDetailScreen(
    lecturaId: Int,
    navController: NavController,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Buscar la lectura seleccionada según el ID provisto por la navegación
    val lectura = state.lecturas.find { it.id == lecturaId } ?: return

    // Requeridor de foco para transferir la selección al botón "Reproducir" inmediatamente al entrar
    val firstBtnFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        firstBtnFocus.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B4A))
            .padding(64.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        // ── Panel Izquierdo: Métricas e Indicador Visual ──
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color(0xFF1565C0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("❤", fontSize = 80.sp)
            }
            Text(
                text = "${lectura.bpm} bpm",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Estado: ${lectura.estado}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = "Hora: ${lectura.hora}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        // ── Panel Derecho: Botones Interactivos de Acción ──
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Botón: Acción de Reproducción
            Surface(
                onClick = { navController.navigate("playback") },
                modifier = Modifier
                    .focusRequester(firstBtnFocus)
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color(0xFF1B5E20),
                    focusedContainerColor = Color(0xFF76FF03)
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "▶ Reproducir",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Botón: Acción de Retorno
            Surface(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color(0xFF37474F),
                    focusedContainerColor = Color(0xFF90A4AE)
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "← Volver", color = Color.White, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}